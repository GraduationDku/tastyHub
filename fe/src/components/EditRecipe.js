import React, { useState, useEffect } from 'react';

function EditRecipe({ recipeId }) {
  const [formData, setFormData] = useState({
    foodName: '',
    foodImgUrl: '',
    foodInformation: ''
  });

  useEffect(() => {
    async function fetchRecipeDetails() {
      try {
        const response = await fetch(`http://localhost:8080/recipe/details/${recipeId}`);
        const data = await response.json({
          foodName,
          foodImg,
          foodInformationId,
          foodInformation,
          ingredients,
          cookSteps
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
      <button type="submit">Update Recipe</button>
    </form>
  );
}

export default EditRecipe;
