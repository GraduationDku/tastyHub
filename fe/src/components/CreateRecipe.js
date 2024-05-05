import React, { useState } from 'react';

function CreateRecipe() {
  const [formData, setFormData] = useState({
    foodName: '',
    foodImgUrl: '',
    foodInformation: '',
    ingredients: '', // 추가된 필드
    cookSteps: '' // 추가된 필드
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const { foodName, foodImgUrl, foodInformation, ingredients, cookSteps } = formData;
      const response = await fetch('http://localhost:8080/recipe/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          foodName,
          foodImgUrl, // 변수 이름 수정됨
          foodInformation,
          ingredients,
          cookSteps
        })
      });
      if (response.ok) {
        const authorization = response.headers.get('Authorization');
        const refreshToken = response.headers.get('Refresh');
        localStorage.setItem('accessToken', authorization);
        localStorage.setItem('refreshToken', refreshToken);
        alert('Recipe created successfully!');
        // Clear form or redirect user
      } else {
        throw new Error('Failed to create recipe');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error creating recipe');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
        Recipe Name:
        <input type="text" name="foodName" value={formData.foodName} onChange={handleChange} />
        <br />
        <br />
      </label>
      <label>
        Image URL:
        <input type="text" name="foodImgUrl" value={formData.foodImgUrl} onChange={handleChange} />
        <br />
        <br />
      </label>
      <label>
        Description:
        <textarea name="foodInformation" value={formData.foodInformation} onChange={handleChange} />
        <br />
        <br />
      </label>
      <label>
        Ingredients:
        <input type="text" name="ingredients" value={formData.ingredients} onChange={handleChange} />
        <br />
        <br />
      </label>
      <label>
        Cooking Steps:
        <textarea name="cookSteps" value={formData.cookSteps} onChange={handleChange} />
        <br />
        <br />
      </label>
      <button type="submit">Create Recipe</button>
    </form>
  );
}

export default CreateRecipe;
