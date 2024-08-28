import React, { useState } from 'react';
import '../../css/Recipe/CreateRecipe.css';

function CreateRecipe() {
  const [form, setForm] = useState({
    foodName: '',
    foodInformation: {
      text: '',
      cookingTime: 0,
      serving: ''
    },
    ingredients: [{ ingredientName: '', amount: '' }],
    cookSteps: [{ stepNumber: 1, text: '' }]
  });
  const [imageFile, setImageFile] = useState(null); // 단일 이미지 파일을 저장할 변수
  const [imagePreview, setImagePreview] = useState(''); // 단일 이미지 미리보기 URL을 저장할 변수

  // 폼 데이터 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'foodName') {
      setForm({ ...form, [name]: value });
    } else if (name === 'text' || name === 'cookingTime' || name === 'serving') {
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
      setForm({ ...form, cookSteps: [...form.cookSteps, { stepNumber: form.cookSteps.length + 1, text: '' }] });
    }
  };

  // 이미지 파일 변경 핸들러
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImageFile(file); // 단일 이미지 파일 저장

    // 이미지 미리보기 URL 생성
    const preview = URL.createObjectURL(file);
    setImagePreview(preview);
  };

  // 폼 제출 핸들러
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // JSON 데이터를 생성
      const data = {
        foodName: form.foodName,
        foodInformation: form.foodInformation,
        ingredients: form.ingredients,
        cookSteps: form.cookSteps
      };
      console.log(data);
      console.log(imageFile);

      // FormData 객체 생성
      const formData = new FormData();
      formData.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }));
      formData.append('img', imageFile);
      console.log(formData);

      const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe/create`, {
        method: 'POST',
        headers: {
          'Authorization': localStorage.getItem('accessToken'),
        },
        body: formData,
      });

      console.log(response);

      // 응답 상태 확인
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
        <div className='label1'>
          레시피 이름 
          <input type="text" name="foodName" value={form.foodName} onChange={handleChange} />
          <br /><br />
        </div>
        <div className='label2'>
          사진 파일 
          <input type="file" accept="image/*" onChange={handleImageChange} />
          <div>
            {imagePreview && (
              <img src={imagePreview} alt="Preview" style={{ width: '100px', height: '100px', margin: '5px' }} />
            )}
          </div>
        </div>
        <div className='label3'>
          <p>레시피 설명</p>
          <textarea name="text" value={form.foodInformation.text} onChange={handleChange} />
          </div><br/>
        <div className='label6'>
          조리 시간 
          <input type="number" name="cookingTime" value={form.foodInformation.cookingTime} onChange={handleChange} />
          <br /><br />
          </div>
        <div className='label7'>
          양 
          <input name="serving" value={form.foodInformation.serving} onChange={handleChange} />
          <br /><br />
        </div>
        <div className='label4'>
          재료
          {form.ingredients.map((ingredient, index) => (
            <div key={index}>
              <input type="text" name="ingredientName" value={ingredient.ingredientName} onChange={(e) => handleArrayChange(e, index, 'ingredients')} placeholder="재료 이름" />
              <input type="text" name="amount" value={ingredient.amount} onChange={(e) => handleArrayChange(e, index, 'ingredients')} placeholder="양" />
            </div>
          ))}
          <button type="button" onClick={() => handleAddArrayItem('ingredients')}>재료 추가</button>
          <br /><br />
        </div>
        <div className='label5'>
          순서
          {form.cookSteps.map((step, index) => (
            <div key={index}>
              <span className='step-number'>{step.stepNumber}  </span><br/>
              <input  name="text" value={step.text} onChange={(e) => handleArrayChange(e, index, 'cookSteps')} placeholder="조리 방법" />
            </div>
          ))}
          <br></br>
          <button type="button" onClick={() => handleAddArrayItem('cookSteps')}>순서 추가</button>
          <br /><br />
        </div>
        <button className='save' type="submit">레시피 저장하기</button>
      </form>
    </div>
  );
}

export default CreateRecipe;
