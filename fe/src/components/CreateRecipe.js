import React, { useState } from 'react';
import '../css/CreateRecipe.css';

function CreateRecipe() {
  const [formData, setFormData] = useState({
    foodName: '',
    foodInformation: '',
    ingredients: '',
    cookSteps: ''
  });
  const [imageFile, setImageFile] = useState(null); // 단일 이미지 파일을 저장할 변수
  const [imagePreview, setImagePreview] = useState(''); // 단일 이미지 미리보기 URL을 저장할 변수

  // 폼 데이터 변경 핸들러
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
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
      // 폼 데이터 객체 생성
      const data = {
        foodName: formData.foodName,
        foodInformation: formData.foodInformation,
        ingredients: formData.ingredients,
        cookSteps: formData.cookSteps
      };
      console.log(data);
      const img = {
        imageFile
      }
      console.log(img);
      


      // 서버로 요청 전송
      const response = await fetch('http://localhost:8080/recipe/create', {
        method: 'POST',
        headers: {
          'Authorization': localStorage.getItem('accessToken')
        },
        body: data,img,
      });

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
        <br/>
        <br/>
        <div className='label1'>
          레시피 이름 :
          <input type="text" name="foodName" value={formData.foodName} onChange={handleChange} />
          <br /><br />
        </div>
        <div className='label2'>
          사진 파일 :
          <input type="file" accept="image/*" onChange={handleImageChange} />
          <div>
            {imagePreview && (
              <img src={imagePreview} alt="Preview" style={{ width: '100px', height: '100px', margin: '5px' }} />
            )}
          </div>
          <br /><br />
        </div>
        <div className='label3'>
          설명 :
          <input name="foodInformation" value={formData.foodInformation} onChange={handleChange} />
          <br /><br />
        </div>
        <div className='label4'>
          재료 :
          <input type="text" name="ingredients" value={formData.ingredients} onChange={handleChange} />
          <br /><br />
        </div>
        <div className='label5'>
          순서 :
          <input name="cookSteps" value={formData.cookSteps} onChange={handleChange} />
          <br /><br />
        </div>
        <button type="submit">Create Recipe</button>
      </form>
    </div>
  );
}

export default CreateRecipe;
