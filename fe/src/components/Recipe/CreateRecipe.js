import React, { useState } from 'react';
import '../../css/CreateRecipe.css';

function CreateRecipe({ setScreen }) {
  const [isPhotoMode, setIsPhotoMode] = useState(true);
  const [isYouTubeMode, setIsYouTubeMode] = useState(false);
  const [form, setForm] = useState({
    recipeType: '',
    foodName: '',
    foodInformation: {
      content: '',
      cookingTime: 0,
      serving: ''
    },
    ingredients: [{ ingredientName: '', amount: '' }],
    cookSteps: [{ stepNumber: 1, timeLine:'000' ,content: '' }],
    foodVideoUrl : ''
  });
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
    } else if (name === 'youtubeUrl') {
      setForm({ ...form, youtubeUrl: value });
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
        cookSteps: [...form.cookSteps, { stepNumber: form.cookSteps.length + 1, content: '' }]
      });
    }
  };

  const handleRemoveArrayItem = (index, type) => {
    const list = [...form[type]];
    list.splice(index, 1);
    setForm({ ...form, [type]: list });
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setFile(file);
    const preview = URL.createObjectURL(file);
    setFilePreview(preview);
  };

  const handleVideoChange = (e) => {
    const file = e.target.files[0];
    setVideoFile(file);
    const preview = URL.createObjectURL(file);
    setVideoPreview(preview);
  };

  const nextStep = () => setCurrentStep((prev) => prev + 1);
  const prevStep = () => setCurrentStep((prev) => prev - 1);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      if (isYouTubeMode) {
        // 유튜브 API 호출
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
          form.cookSteps = data.cookSteps;
          form.foodVideoUrl = data.s3_url;
          form.recipeType = 'Youtube';
        } else {
          throw new Error('유튜브 처리 실패');
        }
      } else if (!isPhotoMode) {

        // FormData 객체 생성
        const formData = new FormData();
        formData.append('foodName', form.foodName);

        // items: '[{"id": 1, "name": "apple"}, {"id": 2, "name": "banana"}, {"id": 3, "name": "cherry"}]'


        // const cookSteps = form.cookSteps.map((step) => ({
        //   stepNumber: step.stepNumber,
        //   timeLine: step.timeLine || "00:00", // 기본값 설정
        //   content: step.content,
        // }));
        // formData.append('cookSteps', JSON.stringify(cookSteps)); // JSON 문자열로 추가

        // cookSteps를 서버가 예상하는 형식에 맞게 추가
        // form.cookSteps.forEach((step, index) => {
        //   formData.append(`cookSteps`, JSON.stringify(step));
        // });
        formData.append('cookSteps',JSON.stringify(form.cookSteps))


        // 파일 첨부
        if (videoFile) {
          formData.append('foodVideo', videoFile);
        }
        for (let [key, value] of formData.entries()) {
          console.log(`${key}:`, value);
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
      }
     else if (isPhotoMode){
      form.recipeType = 'Photo';
     }
      const finalFormData = new FormData();
      finalFormData.append (
        'data'
        ,
        new Blob( [JSON.stringify(form)], { type: 'application/json' })
      );
       if (file) {
        finalFormData.append('img', file);
         }

       for (let [key, value] of finalFormData.entries()) {
        console.log(`${key}:`, value);
        }
            
            

      const finalResponse = await fetch(`${process.env.REACT_APP_API_URL}/recipe/create`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Authorization': localStorage.getItem('accessToken'),
        },
        body: finalFormData,
      });
      console.log(finalResponse);

      if (finalResponse.ok) {
        console.log('최종 레시피 생성', form);
        alert('레시피가 성공적으로 생성되었습니다!');
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
          {currentStep === 1 && (
              <div className="label1">
                <br />
                <h2 className="create">레시피 이름을 작성해주세요 !</h2>
                <input className="label1in" type="text" name="foodName" value={form.foodName} onChange={handleChange} />
                <br /><br /><br />
                <button type="button" onClick={nextStep}>다음</button>
                <br /><br /><br />
              </div>
          )}

          {currentStep === 2 && (
              <div className="label2">
                <br />
                <h2 className="create">대표 사진을 등록해주세요 !</h2>
                <br />
                <input type="file" accept="image/*" onChange={handleFileChange} />
                <br /><br />
                {filePreview && <img src={filePreview} alt="Preview" style={{ width: '100%', height: '50%' }} />}
                <br /><br />
                <button type="button" onClick={prevStep}>이전</button>
                <button type="button" onClick={nextStep}>다음</button>
                <br /><br /><br />
              </div>
          )}

          {currentStep === 3 && (
              <div className="label3">
                <br />
                <h2 className="create">레시피에 대한 설명을 작성해주세요 !</h2>
                <br />
                <textarea className="label3in" name="content" value={form.foodInformation.content} onChange={handleChange} />
                <br /><br />
                <button type="button" onClick={prevStep}>이전</button>
                <button type="button" onClick={nextStep}>다음</button>
                <br /><br /><br />
              </div>
          )}

          {currentStep === 4 && (
              <div className="label4">
                <br />
                <h2 className="create">조리 시간과 양을 적어주세요 !</h2>
                <br />
                <input
                    type="number"
                    name="cookingTime"
                    value={form.foodInformation.cookingTime}
                    onChange={handleChange}
                    placeholder="조리 시간"
                />
                <br /><br />
                <input
                    type="text"
                    name="serving"
                    value={form.foodInformation.serving}
                    onChange={handleChange}
                    placeholder="양"
                />
                <br /><br />
                <button type="button" onClick={prevStep}>이전</button>
                <button type="button" onClick={nextStep}>다음</button>
                <br /><br /><br />
              </div>
          )}

          {currentStep === 5 && (
              <div className="label5">
                <br />
                <h2 className="create">재료는 어떤 것이 필요한가요 ?</h2>
                <br />
                {form.ingredients.map((ingredient, index) => (
                    <div key={index}>
                      <input
                          type="text"
                          name="ingredientName"
                          value={ingredient.ingredientName}
                          onChange={(e) => handleArrayChange(e, index, 'ingredients')}
                          placeholder="재료 이름"
                      />
                      <br /><br />
                      <input
                          type="text"
                          name="amount"
                          value={ingredient.amount}
                          onChange={(e) => handleArrayChange(e, index, 'ingredients')}
                          placeholder="양"
                      />
                      <br /><br />
                      <button type="button" onClick={() => handleRemoveArrayItem(index, 'ingredients')}>삭제</button>
                      <br />
                    </div>
                ))}
                <br />
                <button type="button" onClick={() => handleAddArrayItem('ingredients')}>+</button>
                <br /><br />
                <button type="button" onClick={prevStep}>이전</button>
                <button type="button" onClick={nextStep}>다음</button>
                <br /><br /><br />
              </div>
          )}

          {currentStep === 6 && (
              <div className="label6">
                <br />
                <h2 className="create">조리 단계 입력 방법 선택</h2>
                <br />
                <div className="mode-toggle">
                  <button
                      type="button"
                      onClick={() => {
                        setIsPhotoMode(true);
                        setIsYouTubeMode(false);
                      }}
                      className={isPhotoMode ? 'active' : ''}
                  >
                    사진 입력
                  </button>
                  <button
                      type="button"
                      onClick={() => {
                        setIsPhotoMode(false);
                        setIsYouTubeMode(false);
                      }}
                      className={!isPhotoMode && !isYouTubeMode ? 'active' : ''}
                  >
                    동영상 입력
                  </button>
                  <button
                      type="button"
                      onClick={() => {
                        setIsYouTubeMode(true);
                        setIsPhotoMode(false);
                      }}
                      className={isYouTubeMode ? 'active' : ''}
                  >
                    링크 입력
                  </button>
                </div>
                <br /><br />
                {isPhotoMode ? (
                    <>
                      {form.cookSteps.map((step, index) => (
                          <div key={index}>
                            <br />
                            <span>{step.stepNumber}</span>
                            <input
                                name="content"
                                value={step.content}
                                onChange={(e) => handleArrayChange(e, index, 'cookSteps')}
                                placeholder="조리 단계"
                            />
                            <br /><br />
                            <button type="button" onClick={() => handleRemoveArrayItem(index, 'cookSteps')}>삭제</button>
                            <br /><br />
                          </div>
                      ))}
                      <button type="button" onClick={() => handleAddArrayItem('cookSteps')}>+</button>
                    </>
                ) : isYouTubeMode ? (
                    <>
                      <input
                          type="text"
                          name="youtubeUrl"
                          value={form.foodVideoUrl}
                          onChange={handleChange}
                          placeholder="유튜브 링크 입력"
                      />
                      <br /><br />
                    </>
                ) : (
                    <>
                      <input type="file" accept="video/*" onChange={handleVideoChange} />
                      {videoPreview && <video src={videoPreview} controls style={{ width: '100%', height: '50%' }} />}
                      <br /><br />
                      {form.cookSteps.map((step, index) => (
                          <div key={index}>
                            <br />
                            <span>{step.stepNumber}</span>
                            <input
                                name="content"
                                value={step.content}
                                onChange={(e) => handleArrayChange(e, index, 'cookSteps')}
                                placeholder="조리 단계"
                            />
                            <br /><br />
                            <button type="button" onClick={() => handleRemoveArrayItem(index, 'cookSteps')}>삭제</button>
                            <br /><br />
                          </div>
                      ))}
                      <button type="button" onClick={() => handleAddArrayItem('cookSteps')}>+</button>
                    </>
                )}
                <br /><br />
                <button type="button" onClick={prevStep}>이전</button>
                <button type="submit">제출</button>
                <br /><br /><br />
              </div>
          )}
        </form>
      </div>
  );
}

export default CreateRecipe;
