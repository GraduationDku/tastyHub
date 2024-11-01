import React, { useState } from 'react';
import '../../css/CreateRecipe.css';

function CreateRecipe({ setScreen }) {
  const [isPhotoMode, setIsPhotoMode] = useState(true); // 초기 모드는 사진
  const [form, setForm] = useState({
    foodName: '',
    foodInformation: {
      content: '',
      cookingTime: 0,
      serving: ''
    },
    ingredients: [{ ingredientName: '', amount: '' }],
    cookSteps: [{ stepNumber: 1, content: '' }]
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
      const data = {
        foodName: form.foodName,
        cookSteps: isPhotoMode ? form.cookSteps : []
      };

      const formData = new FormData();
      formData.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }));
      formData.append('img', file); // 대표 사진 파일 추가
      if (!isPhotoMode) formData.append('foodVideo', videoFile); // 조리 단계 동영상 추가

      const url = isPhotoMode
        ? `${process.env.REACT_APP_API_URL}/recipe/create`
        : `${process.env.REACT_APP_API_URL}/recipe/video/timeline`;

      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Authorization': localStorage.getItem('accessToken'),
        },
        body: formData,
      });

      if (response.ok) {
        alert('Recipe created successfully!');
      } else {
        throw new Error('Failed to create recipe');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error creating recipe');
    }
  };

  return (
    <div className='createrecipe'>
      <button className='back-button' onClick={() => setScreen('recipe')}> 돌아가기 </button>

      <form onSubmit={handleSubmit}>
        <br/><br/>
        {currentStep === 1 && (
          <div className='label1'>
            <h2>레시피 이름을 작성해주세요 !</h2>
            <input type="text" name="foodName" value={form.foodName} onChange={handleChange} />
            <button type="button" onClick={nextStep}>다음</button>
          </div>
        )}

        {currentStep === 2 && (
          <div className='label2'>
            <h2>대표 사진을 등록해주세요 !</h2>
            <input type="file" accept="image/*" onChange={handleFileChange} />
            {filePreview && <img src={filePreview} alt="Preview" style={{ width: '100%', height: '50%' }} />}
            <button type="button" onClick={prevStep}>이전</button>
            <button type="button" onClick={nextStep}>다음</button>
          </div>
        )}

        {currentStep === 3 && (
          <div className='label3'>
            <h2>레시피에 대한 설명을 작성해주세요 !</h2>
            <textarea name="content" value={form.foodInformation.content} onChange={handleChange} />
            <button type="button" onClick={prevStep}>이전</button>
            <button type="button" onClick={nextStep}>다음</button>
          </div>
        )}

        {currentStep === 4 && (
          <div className='label6'>
            <h2>조리 시간과 양은 얼마나 되나요 ?</h2>
            <input type='number' name='cookingTime' value={form.foodInformation.cookingTime} onChange={handleChange} placeholder='조리 시간' />
            <input type='text' name='serving' value={form.foodInformation.serving} onChange={handleChange} placeholder='양' />
            <button type="button" onClick={prevStep}>이전</button>
            <button type="button" onClick={nextStep}>다음</button>
          </div>
        )}

        {currentStep === 5 && (
          <div className='label5'>
            <h2>재료는 어떤 것이 필요한가요 ?</h2>
            {form.ingredients.map((ingredient, index) => (
              <div key={index}>
                <input type="text" name="ingredientName" value={ingredient.ingredientName} onChange={(e) => handleArrayChange(e, index, 'ingredients')} placeholder="재료 이름" />
                <input type="text" name="amount" value={ingredient.amount} onChange={(e) => handleArrayChange(e, index, 'ingredients')} placeholder="양" />
                <button type="button" onClick={() => handleRemoveArrayItem(index, 'ingredients')}>삭제</button>
              </div>
            ))}
            <button type="button" onClick={() => handleAddArrayItem('ingredients')}>+</button>
            <button type="button" onClick={prevStep}>이전</button>
            <button type="button" onClick={nextStep}>다음</button>
          </div>
        )}

        {currentStep === 6 && (
          <div className='label5'>
            <h2>조리 단계 입력 방법 선택</h2>
            <div className="mode-toggle">
              <button type="button" onClick={() => setIsPhotoMode(true)} className={isPhotoMode ? 'active' : ''}>사진 입력</button>
              <button type="button" onClick={() => setIsPhotoMode(false)} className={!isPhotoMode ? 'active' : ''}>동영상 입력</button>
            </div>
            
            {isPhotoMode ? (
              form.cookSteps.map((step, index) => (
                <div key={index}>
                  <span>{step.stepNumber}</span>
                  <input name="content" value={step.content} onChange={(e) => handleArrayChange(e, index, 'cookSteps')} placeholder="조리 단계" />
                  <button type="button" onClick={() => handleRemoveArrayItem(index, 'cookSteps')}>삭제</button>
                </div>
              ))
            ) : (
              <input type="file" accept="video/*" onChange={handleVideoChange} />
            )}
            {isPhotoMode && <button type="button" onClick={() => handleAddArrayItem('cookSteps')}>+</button>}
            {videoPreview && !isPhotoMode && <video src={videoPreview} controls style={{ width: '100%', height: '50%' }} />}
            <button type="button" onClick={prevStep}>이전</button>
            <button type="submit">제출</button>
          </div>
        )}
      </form>
    </div>
  );
}

export default CreateRecipe;
