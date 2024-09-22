import React, { useState } from 'react';
import '../../css/CreateRecipe.css';

function CreateRecipe() {
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
  const [imageFile, setImageFile] = useState(null); // 단일 이미지 파일을 저장할 변수
  const [imagePreview, setImagePreview] = useState(''); // 단일 이미지 미리보기 URL을 저장할 변수
  const [currentStep, setCurrentStep] = useState(1); // 현재 단계

  // 폼 데이터 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'foodName') {
      setForm({ ...form, [name]: value });
    } else if (name === 'content' || name === 'cookingTime' || name === 'serving') {
      setForm({ ...form, foodInformation: { ...form.foodInformation, [name]: value } });
    }
  };

  // 재료와 조리 단계 추가 및 변경 핸들러
  const handleArrayChange = (e, index, type) => {
    const { name, value } = e.target;
    const list = [...form[type]];
    list[index][name] = value;
    setForm({ ...form, [type]: list });
  };

  const handleAddArrayItem = (type) => {
    if (type === 'ingredients') {
      setForm({ ...form, ingredients: [...form.ingredients, { ingredientName: '', amount: '' }] });
    } else if (type === 'cookSteps') {
      setForm({ ...form, cookSteps: [...form.cookSteps, { stepNumber: form.cookSteps.length + 1, content: '' }] });
    }
  };

  // 배열 항목 삭제 핸들러 (재료 및 조리 단계 삭제 기능 추가)
  const handleRemoveArrayItem = (index, type) => {
    const list = [...form[type]];
    list.splice(index, 1);
    setForm({ ...form, [type]: list });
  };

  // 이미지 파일 변경 핸들러
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImageFile(file); // 단일 이미지 파일 저장
    const preview = URL.createObjectURL(file); // 이미지 미리보기 URL 생성
    setImagePreview(preview);
  };

  const nextStep = () => {
    setCurrentStep((prev) => prev + 1);
  };

  const prevStep = () => {
    setCurrentStep((prev) => prev - 1);
  };

  // 폼 제출 핸들러
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = {
        foodName: form.foodName,
        foodInformation: form.foodInformation,
        ingredients: form.ingredients,
        cookSteps: form.cookSteps
      };
      const formData = new FormData();
      formData.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }));
      formData.append('img', imageFile);

      const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe/create`, {
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
      <form onSubmit={handleSubmit}>
        {currentStep === 1 && (
          <div className='label1'>
            <br/>
            <h2>레시피 이름을 작성해주세요 !</h2>
            <br/>
            <input className='label1in' type="text" name="foodName" value={form.foodName} onChange={handleChange} />
            <br /><br /><br/>
            <button type="button" onClick={nextStep}>다음</button>
            <br/><br/><br/>
          </div>
        )}

        {currentStep === 2 && (
          <div className='label2'>
            <br/>
            <h2>대표 사진을 등록해주세요 !</h2>
            <br/>
            <input className='label2in' type="file" accept="image/*" onChange={handleImageChange} />
            <div>
              {imagePreview && (
                <img src={imagePreview} alt="Preview" style={{ width: '100px', height: '100px', margin: '5px' }} />
              )}
            </div>
            <br/><br/>
            <button type="button" onClick={prevStep}>이전</button>
            <button type="button" onClick={nextStep}>다음</button>
            <br/><br/><br/>
          </div>
        )}

        {currentStep === 3 && (
          <div className='label3'>
            <br/>
            <h2>레시피에 대한 설명을 작성해주세요 !</h2>
            <br/>
            <textarea className='label3in' name="content" value={form.foodInformation.content} onChange={handleChange} />
            <br/><br/>
            <br />
            <button type="button" onClick={prevStep}>이전</button>
            <button type="button" onClick={nextStep}>다음</button>
            <br/><br/><br/>
          </div>
        )}

        {currentStep === 4 && (
          <div className='label6'>
            <br/>
            <h2>조리 시간과 양은 얼마나 되나요 ?</h2>
            <br/>
            조리 시간
            <input className='label4in' type="number" name="cookingTime" value={form.foodInformation.cookingTime} onChange={handleChange} />
            <br /><br />
            양
            <input className='label4in' name="serving" value={form.foodInformation.serving} onChange={handleChange} />
            <br /><br />
            <br/>
            <button type="button" onClick={prevStep}>이전</button>
            <button type="button" onClick={nextStep}>다음</button>
            <br/><br/><br/>
          </div>
        )}

        {currentStep === 5 && (
          <div className='label4'>
            <br/>
            <h2>재료는 어떤 것이 필요한가요 ?</h2>
            <br/>
            {form.ingredients.map((ingredient, index) => (
              <div key={index}>
                <input
                  type="text"
                  name="ingredientName"
                  value={ingredient.ingredientName}
                  onChange={(e) => handleArrayChange(e, index, 'ingredients')}
                  placeholder="재료 이름"
                  className='label5in'
                />
                <br/><br/>
                <input
                  type="text"
                  name="amount"
                  value={ingredient.amount}
                  onChange={(e) => handleArrayChange(e, index, 'ingredients')}
                  placeholder="양"
                  className='label5in'
                /><br/><br/>
                <button type="button" onClick={() => handleRemoveArrayItem(index, 'ingredients')}>삭제</button>
              </div>
            ))}
            <br/>
            <button type="button" onClick={() => handleAddArrayItem('ingredients')}>재료 추가</button>
            <br /><br /><br/>
            <button type="button" onClick={prevStep}>이전</button>
            <button type="button" onClick={nextStep}>다음</button>
            <br/><br/><br/>
          </div>
        )}

        {currentStep === 6 && (
          <div className='label5'>
            <br/>
            <h2>조리 순서를 작성해주세요 !</h2>
            <br/>
            {form.cookSteps.map((step, index) => (
              <div key={index}>
                <span className='step-number'>{step.stepNumber}</span>
                <input
                  name="content"
                  value={step.content}
                  onChange={(e) => handleArrayChange(e, index, 'cookSteps')}
                  placeholder="조리 단계"
                  className='label6in'
                />
                <br/><br/>
                <button type="button" onClick={() => handleRemoveArrayItem(index, 'cookSteps')}>삭제</button>
                <br/><br/>
              </div>
            ))}
            <br/>
            <button type="button" onClick={() => handleAddArrayItem('cookSteps')}>순서 추가</button>
            <br /><br />
            <button type="button" onClick={prevStep}>이전</button>
            <button type="submit" className='save'>레시피 저장하기</button>
            <br/><br/><br/>
          </div>
        )}
      </form>
    </div>
  );
}

export default CreateRecipe;
