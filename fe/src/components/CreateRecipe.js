import React, { useState } from 'react';

function CreateRecipe() {
  const [formData, setFormData] = useState({
    foodName: '',
    foodImgUrl: '',
    foodInformation: ''
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
      const response = await fetch('http://localhost:8080/recipe/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          foodName,
          foodImg,
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
      </label>
      <label>
        Image URL:
        <input type="text" name="foodImgUrl" value={formData.foodImgUrl} onChange={handleChange} />
      </label>
      <label>
        Description:
        <textarea name="foodInformation" value={formData.foodInformation} onChange={handleChange} />
      </label>
      <button type="submit">Create Recipe</button>
    </form>
  );
}

export default CreateRecipe;
