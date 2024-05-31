import React, { useState, useEffect } from 'react';
import '../css/RecipeDetails.css';
import CreateRecipeReview from './CreateRecipeReview'; // CreateRecipeReview 컴포넌트 불러오기

function RecipeDetails({ recipeId }) {
  const [recipeDetails, setRecipeDetails] = useState(null);

  useEffect(() => {
    async function fetchRecipeDetails() {
      try {
        const response = await fetch(`http://localhost:8080/recipe/detail/${recipeId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken')
          }
        });
        if (response.ok) {
          const data = await response.json();
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
      <h1>{recipeDetails.foodName}</h1>
      <div className='box'>
        <img src={recipeDetails.foodImgUrl} alt={recipeDetails.foodName} />

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

        {/* 리뷰 작성 컴포넌트를 여기에 추가 */}
        <div>
          <h3>Write a Review:</h3>
          <CreateRecipeReview recipeId={recipeId} />
        </div>
      </div>
    </div>
  );
}

export default RecipeDetails;
