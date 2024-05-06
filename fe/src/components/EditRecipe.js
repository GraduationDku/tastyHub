import React, { useState, useEffect } from 'react';

function EditRecipe({ recipeId }) {
  const [formData, setFormData] = useState({
    foodName: '',
    foodImgUrl: '',
    foodInformation: '',
    ingredients: '', // 추가된 필드
    cookSteps: '' // 추가된 필드
  });

  useEffect(() => {
    async function fetchRecipeDetails() {
      try {
        const response = await fetch(`http://localhost:8080/recipe/details/${recipeId}`);
        console.log('Request URL:', response.url); // 요청 URL 로그
        const data = await response.json();
        setFormData({
          foodName: data.foodName || '',
          foodImgUrl: data.foodImgUrl || '',
          foodInformation: data.foodInformation || '',
          ingredients: data.ingredients || '',
          cookSteps: data.cookSteps || ''
        });
      } catch (error) {
        console.error('Error fetching recipe details:', error);
      }
    }

    fetchRecipeDetails();
  }, [recipeId]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`http://localhost:8080/recipe/update/${recipeId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });
      if (response.ok) {
        alert('Recipe updated successfully!');
      } else {
        throw new Error('Failed to update recipe');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error updating recipe');
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
      {/* 추가된 필드들 */}
      <label>
        Ingredients:
        <input type="text" name="ingredients" value={formData.ingredients} onChange={handleChange} />
      </label>
      <label>
        Cook Steps:
        <input type="text" name="cookSteps" value={formData.cookSteps} onChange={handleChange} />
      </label>
      <button type="submit">Update Recipe</button>
    </form>
  );
}

export default EditRecipe;
