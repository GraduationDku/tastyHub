import React, { useState } from 'react';
import { BiLogoYoutube } from 'react-icons/bi';
import { BiVideo } from 'react-icons/bi';
import { MdPhotoAlbum } from 'react-icons/md';
import '../../css/CreateRecipe.css';

function CreateRecipe({ setScreen }) {
  const [isPhotoMode, setIsPhotoMode] = useState(true);
  const [isYouTubeMode, setIsYouTubeMode] = useState(false);
  const [form, setForm] = useState({
    recipeType: '',
    foodName: '',
    foodInformation: {
      content: '',
      cookingTime: '',
      serving: ''
    },
    ingredients: [{ ingredientName: '', amount: '' }],
    cookSteps: [{ stepNumber: 1, timeLine: '00:00:00', content: '' }],
    foodVideoUrl: ''
  });
  const [cookStepImgs, setCookStepImgs] = useState([]); // 쿡스텝 이미지 리스트
  const [file, setFile] = useState(null);
  const [filePreview, setFilePreview] = useState('');
  const [videoFile, setVideoFile] = useState(null);
  const [videoPreview, setVideoPreview] = useState('');
  const [currentStep, setCurrentStep] = useState(1);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'foodName') {
      setForm({ ...form, [name]: value });
    } else if (name === 'content' || name === 'cookingTime' || name === 'serving') {
      setForm({ ...form, foodInformation: { ...form.foodInformation, [name]: value } });
    } else if (name === 'foodVideoUrl') { // 수정됨
      setForm({ ...form, foodVideoUrl: value });
    }
  };

  const handleArrayChange = (e, index, type) => {
    const { name, value } = e.target;
    const list = [...form[type]];
    list[index][name] = value;
    setForm({ ...form, [type]: list });
  };

  const handleAddArrayItem = (type) => {
    if (type === 'ingredients') {
      setForm({
        ...form,
        ingredients: [...form.ingredients, { ingredientName: '', amount: '' }]
      });
    } else if (type === 'cookSteps') {
      setForm({
        ...form,
        cookSteps: [
          ...form.cookSteps,
          { stepNumber: form.cookSteps.length + 1, timeLine: '00:00:00', content: '' }
        ]
      });
      setCookStepImgs([...cookStepImgs, null]); // 새로운 쿡스텝에 대한 이미지 자리 추가
    }
  };

  const handleRemoveArrayItem = (index, type) => {
    const list = [...form[type]];
    list.splice(index, 1);
    setForm({ ...form, [type]: list });
    if (type === 'cookSteps') {
      const imgList = [...cookStepImgs];
      imgList.splice(index, 1); // 이미지 리스트에서도 삭제
      setCookStepImgs(imgList);
    }
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setFile(file);
    const preview = URL.createObjectURL(file);
    setFilePreview(preview);
  };

  const handleCookStepImgChange = (e, index) => {
    const file = e.target.files[0];
    if (!file) return;
    const newCookStepImgs = [...cookStepImgs];
    newCookStepImgs[index] = file; // 해당 index에 이미지 추가
    setCookStepImgs(newCookStepImgs);
  };

  const handleVideoChange = (e) => {
    const file = e.target.files[0];
    setVideoFile(file);
    const preview = URL.createObjectURL(file);
    setVideoPreview(preview);
  };

  const nextStep = () => setCurrentStep((prev) => prev + 1);
  const prevStep = () => setCurrentStep((prev) => prev - 1);

  function isValidYouTubeUrl(url) {
    const pattern = /^(https?:\/\/)?(www\.)?(youtube\.com|youtu\.be)\/.+$/;
    return pattern.test(url);
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (isYouTubeMode && !isValidYouTubeUrl(form.foodVideoUrl)) {
      alert('유효한 유튜브 URL을 입력하세요.');
      return;
    }

    try {
      if (isYouTubeMode) {
        const youtubeResponse = await fetch(`${process.env.REACT_APP_API_URL}/video/youtube/link`, {
          method: 'POST',
          headers: {
            'Authorization': localStorage.getItem('accessToken'),
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ youtubeUrl: form.foodVideoUrl }),
        });

        if (youtubeResponse.ok) {
          const data = await youtubeResponse.json();
          form.foodName = data.foodName;
          form.foodInformation = data.foodInformation;
          form.ingredients = data.ingredients;
          form.cookSteps = data.cookSteps;
          console.log('폼',form)
          console.log('데이터',data)
        } else {
          throw new Error('유튜브 처리 실패');
        }
      }else if (!isPhotoMode) {
        // 동영상 API 호출
        const formData = new FormData();
        formData.append('foodName', form.foodName);
        formData.append('cookSteps', JSON.stringify(form.cookSteps));
        if (videoFile) {
          formData.append('foodVideo', videoFile);
        }
        const videoResponse = await fetch(`${process.env.REACT_APP_API_URL}/video/media/action`, {
          method: 'POST',
          headers: {
            'Authorization': localStorage.getItem('accessToken'),
          },
          body: formData,
        });

        if (videoResponse.ok) {
          const data = await videoResponse.json();
          form.cookSteps = data.cookSteps;
          form.foodVideoUrl = data.s3_url;
          form.recipeType = 'Video';
        } else {
          throw new Error('동영상 처리 실패');
        }
      } else if (isPhotoMode) {
        // 사진 모드 로직
        form.recipeType = 'Image';

        // 모든 cookSteps의 timeLine을 '00:00:00'으로 설정
        form.cookSteps = form.cookSteps.map((step) => ({
          ...step,
          timeLine: '00:00:00'
        }));
      }

      const finalFormData = new FormData();
      finalFormData.append(
        'data',
        new Blob([JSON.stringify(form)], { type: 'application/json' })
      );
      if (file) {
        finalFormData.append('recipeImg', file);
      }
      
      if (!isPhotoMode && !isYouTubeMode) { // 사용자 영상 모드일 때
        finalFormData.append('cookStepImgs', new Blob([], { type: 'application/octet-stream' })); // 빈 Blob 추가
      } else if (cookStepImgs.length === 0) { // 사진 모드에서 cookStepImgs가 비어 있을 때
        finalFormData.append('cookStepImgs', new Blob([], { type: 'application/octet-stream' })); // 빈 Blob 추가
      } else {
        cookStepImgs.forEach((img) => {
          if (img) {
            finalFormData.append('cookStepImgs', img);
          }
        });
      }
      
      
      console.log(cookStepImgs);
      console.log(form.recipeType);
      console.log(form.foodInformation);
      console.log(form.foodName);
      console.log(form.cookSteps);
      console.log(form.ingredients);

      const finalResponse = await fetch(`${process.env.REACT_APP_API_URL}/recipe/create`, {
        method: 'POST',
        headers: {
          Authorization: localStorage.getItem('accessToken'),
        },
        body: finalFormData,
      });

      if (finalResponse.ok) {
        alert('레시피가 성공적으로 생성되었습니다!');
        setScreen('recipe'); // 화면 전환
      } else {
        throw new Error('최종 레시피 생성 실패');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('레시피 생성 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="createrecipe">
      <button className="back-button" onClick={() => setScreen('recipe')}>&lt;</button>
      <br /><br />
      <form onSubmit={handleSubmit}>
        {/* Step 1: 작성 방식 선택 */}
        {currentStep === 1 && (
          <div className="label0">
            <br />
            <h2 className="create">레시피를 어떤 방식으로 작성할까요?</h2>
            <div className="mode-toggle">
              <div
                style={{
                  position: 'relative',
                  width: '230px',
                  height: '40px',
                  backgroundColor: '#f0f0f0',
                  borderRadius: '25px',
                  display: 'flex',
                  alignItems: 'center',
                  padding: '5px',
                  boxShadow: '0 2px 4px rgba(0, 0, 0, 0.2)',
                }}
              >
                <div
                  style={{
                    position: 'absolute',
                    top: '5px',
                    left: isPhotoMode ? '5px' : isYouTubeMode ? '165px' : '85px',
                    width: '70px',
                    height: '40px',
                    backgroundColor: '#3EAB5C',
                    borderRadius: '20px',
                    transition: 'left 0.3s ease-in-out',
                  }}
                ></div>
                <br/><br/>
                <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
                  <button
                    type="button"
                    onClick={() => {
                      setIsPhotoMode(true);
                      setIsYouTubeMode(false);
                      setForm({ ...form, recipeType: 'Photo' });
                    }}
                    style={{
                      flex: 1,
                      backgroundColor: 'transparent',
                      border: 'none',
                      color: isPhotoMode ? '#3EAB5C' : '#000',
                      fontWeight: 'normal',
                      textAlign: 'center',
                      zIndex: 1,
                      cursor: 'pointer',
                    }}
                  >
                    <MdPhotoAlbum />
                  </button>
                  <button
                    type="button"
                    onClick={() => {
                      setIsPhotoMode(false);
                      setIsYouTubeMode(false);
                      setForm({ ...form, recipeType: 'Video' });
                    }}
                    style={{
                      flex: 1,
                      backgroundColor: 'transparent',
                      border: 'none',
                      color: !isPhotoMode && !isYouTubeMode ? '#3EAB5C' : '#000',
                      fontWeight: 'normal',
                      textAlign: 'center',
                      zIndex: 1,
                      cursor: 'pointer',
                    }}
                  >
                    <BiVideo />
                  </button>
                  <button
                    type="button"
                    onClick={() => {
                      setIsYouTubeMode(true);
                      setIsPhotoMode(false);
                      setForm({ ...form, recipeType: 'Youtube' });
                    }}
                    style={{
                      flex: 1,
                      backgroundColor: 'transparent',
                      border: 'none',
                      color: isYouTubeMode ? '#3EAB5C' : '#000',
                      fontWeight: 'normal',
                      textAlign: 'center',
                      zIndex: 1,
                      cursor: 'pointer',
                    }}
                  >
                    <BiLogoYoutube />
                  </button>
                </div>
              </div>
            </div>
            <br /><br /><br />
            <button type="button" onClick={nextStep} style={{ width: '50%', marginLeft: '120px' }}>
              다음
            </button>
            <br />
            <br />
            <br />
          </div>
        )}
  
        {/* 유튜브 모드 - Step 2: 대표 사진과 유튜브 링크 입력 */}
        {isYouTubeMode && currentStep === 2 && (
          <div className="labelYoutube">
            <br />
            <h2 className="create">대표 사진을 등록해주세요!</h2>
            <br />
            <input type="file" accept="image/*" onChange={handleFileChange} />
            <br /><br />
            {filePreview && <img src={filePreview} alt="Preview" style={{ width: '100%', height: '50%' }} />}
            <br /><br />
            
            <h2 className="create">조리 시간과 몇 인분인지 적어주세요!</h2>
            <br />
            <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
              <input
                type="text"
                name="cookingTime"
                value={form.foodInformation.cookingTime}
                onChange={handleChange}
                placeholder="조리 시간 (분)"
              />
              <input
                type="text"
                name="serving"
                value={form.foodInformation.serving}
                onChange={handleChange}
                placeholder="몇 인분인가요?"
              />
            </div>
            <br /><br />
            <h2 className="create">유튜브 링크를 입력해주세요!</h2>
            <input
              type="text"
              name="foodVideoUrl"
              value={form.foodVideoUrl}
              onChange={handleChange}
              placeholder="유튜브 링크"
            />
            <br /><br />
            <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
              <button type="button" onClick={prevStep} style={{ width: '50%' }}>
                이전
              </button>
              <button type="submit" style={{ width: '50%' }}>
                제출
              </button>
            </div>
            <br /><br />
          </div>
        )}

  
        {/* Photo/Video 모드 - Step 2: 레시피 이름 입력 */}
        {!isYouTubeMode && currentStep === 2 && (
          <div className="label1">
            <br />
            <h2 className="create">레시피 이름을 작성해주세요!</h2>
            <input className="label1in" type="text" name="foodName" value={form.foodName} onChange={handleChange} />
            <br /><br />
            <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
            <button type="button" onClick={prevStep} style={{ width: '50%' }}>
                이전 
            </button>
            <button type="button" onClick={nextStep} style={{ width: '50%', marginLeft: '10px' }}>
              다음
            </button></div>
            <br /><br /><br />
          </div>
        )}
  
        {/* Photo/Video 모드 - Step 3: 대표 사진 입력 */}
        {!isYouTubeMode && currentStep === 3 && (
          <div className="label2">
            <br />
            <h2 className="create">대표 사진을 등록해주세요!</h2>
            <br />
            <input type="file" accept="image/*" onChange={handleFileChange} />
            <br /><br />
            {filePreview && <img src={filePreview} alt="Preview" style={{ width: '100%', height: '50%' }} />}
            <br /><br />
            <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
              <button type="button" onClick={prevStep} style={{ width: '50%' }}>
                이전
              </button>
              <button type="button" onClick={nextStep} style={{ width: '50%' }}>
                다음
              </button>
            </div>
            <br /><br /><br />
          </div>
        )}
  
        {/* Photo/Video 모드 - Step 4: 레시피 설명 입력 */}
        {!isYouTubeMode && currentStep === 4 && (
          <div className="label3">
            <br />
            <h2 className="create">레시피에 대한 설명을 작성해주세요!</h2>
            <br />
            <textarea
              className="label3in"
              name="content"
              value={form.foodInformation.content}
              onChange={handleChange}
            />
            <br /><br />
            <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
              <button type="button" onClick={prevStep} style={{ width: '50%' }}>
                이전
              </button>
              <button type="button" onClick={nextStep} style={{ width: '50%' }}>
                다음
              </button>
            </div>
            <br /><br /><br />
          </div>
        )}
  
        {/* Photo/Video 모드 - Step 5: 조리 시간과 인분 입력 */}
        {!isYouTubeMode && currentStep === 5 && (
          <div className="label4">
            <br />
            <h2 className="create">조리 시간과 몇 인분인지 적어주세요!</h2>
            <br />
            <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
              <input
                type="text"
                name="cookingTime"
                value={form.foodInformation.cookingTime}
                onChange={handleChange}
                placeholder="조리 시간 (분)"
              />
              <input
                type="text"
                name="serving"
                value={form.foodInformation.serving}
                onChange={handleChange}
                placeholder="몇 인분인가요?"
              />
            </div>
            <br /><br />
            <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
              <button type="button" onClick={prevStep} style={{ width: '50%' }}>
                이전
              </button>
              <button type="button" onClick={nextStep} style={{ width: '50%' }}>
                다음
              </button>
            </div>
            <br /><br /><br />
          </div>
        )}
  
        {/* Photo/Video 모드 - Step 6: 재료 입력 */}
        {!isYouTubeMode && currentStep === 6 && (
          <div className="label5">
            <br />
            <h2 className="create">재료는 어떤 것이 필요한가요?</h2>
            <br />
            {form.ingredients.map((ingredient, index) => (
              <div key={index}>
                <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
                  <input
                    type="text"
                    name="ingredientName"
                    value={ingredient.ingredientName}
                    onChange={(e) => handleArrayChange(e, index, 'ingredients')}
                    placeholder="재료 이름"
                  />
                  <input
                    type="text"
                    name="amount"
                    value={ingredient.amount}
                    onChange={(e) => handleArrayChange(e, index, 'ingredients')}
                    placeholder="양"
                  />
                  <button
                    type="button"
                    onClick={() => handleRemoveArrayItem(index, 'ingredients')}
                    style={{
                      width: '18%',
                      padding: '8px',
                      backgroundColor: 'white',
                      color: '#3EAB5C',
                      border: '1.5px solid #3EAB5C',
                      borderRadius: '100px',
                      textAlign: 'center',
                      marginBottom: '30px',
                    }}
                  >
                    X
                  </button>
                </div>
              </div>
            ))}
            <button
              type="button"
              onClick={() => handleAddArrayItem('ingredients')}
              style={{
                width: '100%',
                backgroundColor: 'white',
                color: '#3EAB5C',
                border: '1.5px solid #3EAB5C',
                textAlign: 'center',
              }}
            >
              +
            </button>
            <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
              <button type="button" onClick={prevStep} style={{ width: '50%' }}>
                이전
              </button>
              <button type="button" onClick={nextStep} style={{ width: '50%' }}>
                다음
              </button>
            </div>
            <br /><br /><br />
          </div>
        )}
  
        {/* Photo/Video 모드 - Step 7: 조리 단계 입력 */}
        {!isYouTubeMode && currentStep === 7 && (
           <div className="label6">
           <br />
           <h2 className="create">조리 단계를 입력하세요 !</h2>
           {isPhotoMode ? (
               <>
                 {form.cookSteps.map((step, index) => (
                     <div key={index}>
                       <br />
                       <button type="button" onClick={() => handleRemoveArrayItem(index, 'cookSteps')}
                     style={{
                       width: '18%',
                       padding:'8px',
                       backgroundColor:'white',
                       color : '#3EAB5C',
                       border: '1.5px solid #3EAB5C',
                       borderRadius : '100px',
                       textAlign : 'center',
                     }}>X</button>
                       <input
                           name="content"
                           value={step.content}
                           onChange={(e) => handleArrayChange(e, index, 'cookSteps')}
                           placeholder={step.stepNumber}
                       />
                       <input
                         type="file"
                         accept="image/*"
                         onChange={(e) => handleCookStepImgChange(e, index)}
                       />
                     </div>
                 ))}
                 <button type="button" onClick={() => handleAddArrayItem('cookSteps')}
                   style={{
                     width: '100%',
                     backgroundColor:'white',
                     color : '#3EAB5C',
                     border: '1.5px solid #3EAB5C',
                     textAlign : 'center',
                     }}>+</button>
               </>
           ) : isYouTubeMode ? (
               <>
               <br/>
                 <input
                     type="text"
                     name="youtubeUrl"
                     value={form.foodVideoUrl}
                     onChange={handleChange}
                     placeholder="유튜브 링크 입력"
                 />
                 <br />
               </>
           ) : (
               <>
                 <br/>
                 <input type="file" accept="video/*" onChange={handleVideoChange} />
                 {videoPreview && <video src={videoPreview} controls style={{ width: '100%', height: '50%' }} />}
                 {form.cookSteps.map((step, index) => (
                     <div key={index}>
                       <br />
                       <button type="button" onClick={() => handleRemoveArrayItem(index, 'cookSteps')}
                   style={{
                     width: '18%',
                     padding:'8px',
                     backgroundColor:'white',
                     color : '#3EAB5C',
                     border: '1.5px solid #3EAB5C',
                     borderRadius : '100px',
                     textAlign : 'center',
                   }}>X</button>
                       <input
                           name="content"
                           value={step.content}
                           onChange={(e) => handleArrayChange(e, index, 'cookSteps')}
                           placeholder={step.stepNumber}
                       />
                     </div>
                 ))}
                 <button type="button" onClick={() => handleAddArrayItem('cookSteps')}
                   style={{
                     width: '100%',
                     backgroundColor:'white',
                     color : '#3EAB5C',
                     border: '1.5px solid #3EAB5C',
                     textAlign : 'center',
                     }}>+</button>
               </>
           )}
           <div style={{ display: 'flex', flexDirection: 'row',
           alignItems: 'center', justifyContent: 'space-between'}}>
           <button type="button" onClick={prevStep} style={{
             width: '50%'
             }}>
             이전
           </button>
           <button type="submit" style={{
             width: '50%'
             }}>
             제출
           </button>
         </div>
           <br /><br /><br />
         </div>
     )}
      </form>
    </div>
  );
  
}

export default CreateRecipe; 