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

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImageFile(file); // 단일 이미지 파일 저장

    // 이미지 미리보기 URL 생성
    const preview = URL.createObjectURL(file);
    setImagePreview(preview);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formdata = new FormData();
      formdata.append('foodName', formData.foodName);
      formdata.append('foodInformation', formData.foodInformation);
      formdata.append('ingredients', formData.ingredients);
      formdata.append('cookSteps', formData.cookSteps);

      if (imageFile) {
        formdata.append('image', imageFile); // Correctly append the image file
      }

      const response = await fetch('http://localhost:8080/recipe/create', {
        method: 'POST',
        headers: {
          'Authorization': localStorage.getItem('accessToken')
        },
        body: formdata
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
