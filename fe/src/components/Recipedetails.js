import React, { useState, useEffect } from 'react';
import '../css/RecipeDetails.css';

function RecipeDetails({ recipeId }) {
  const [recipeDetails, setRecipeDetails] = useState(null);

  useEffect(() => {
    async function fetchRecipeDetails() {
      try {
        const response = await fetch(`http://localhost:8080/recipe/detail/${recipeId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });
        if (response.ok) {
          const data = await response.json();
          const authorization = response.headers.get('Authorization');
          const refreshToken = response.headers.get('Refresh');
          localStorage.setItem('accessToken', authorization);
          localStorage.setItem('refreshToken', refreshToken);
          setRecipeDetails(data);
        } else {
          throw new Error('Failed to fetch recipe details');
        }
      } catch (error) {
        console.error('Error fetching recipe details:', error);
      }
    }

    if (recipeId) {
      fetchRecipeDetails();
    }
  }, [recipeId]);

  if (!recipeDetails) {
    return <p>Loading...</p>;
  }

  return (
    <div className='recipedetails'>
      
      <h1>{recipeDetails.foodName}</h1><div className='box'>
      <img src={recipeDetails.foodImgUrl} alt={recipeDetails.foodName}  />
      
      <div>
        <p><strong>Description:</strong> {recipeDetails.foodInformation.text}</p>
        <p><strong>Cooking Time:</strong> {recipeDetails.foodInformation.cookingTime} minutes</p>
        <p><strong>Serving:</strong> {recipeDetails.foodInformation.serving}</p>
      </div>
      
      <div>
        <h3>Ingredients:</h3>
        <ul>
          {recipeDetails.ingredients.map((ingredient, index) => (
            <li key={ingredient.ingredientId || index}>
              {ingredient.ingredientName} - {ingredient.amount}
            </li>
          ))}
        </ul>
      </div>
      
      <div>
        <h3>Cooking Steps:</h3>
        <ol>
          {recipeDetails.cookSteps.map((step, index) => (
            <li key={step.cookStepId || index}>
              <strong>Step {step.stepNumber}:</strong> {step.text}
              {step.stepImgUrl && <img src={step.stepImgUrl} alt={`Step ${step.stepNumber}`} style={{ width: '100%', maxHeight: '200px' }} />}
            </li>
          ))}
        </ol>
      </div>
    </div>
    </div>
  );
}

export default RecipeDetails;
