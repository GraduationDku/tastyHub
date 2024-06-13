import React, { useState, useEffect } from 'react';
import '../css/RecipeDetails.css';
import CreateRecipeReview from './CreateRecipeReview';
import LikeButton from './Like'; // Ensure you import the LikeButton component correctly
import ScrapButton from './Scrap'; // Import the ScrapButton component

function RecipeDetails({ recipeId }) {
  const [recipeDetails, setRecipeDetails] = useState(null);
  const [recipeReviews, setRecipeReviews] = useState([]);

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
          console.log(data);
        } else {
          throw new Error('Failed to fetch recipe details');
        }
      } catch (error) {
        console.error('Error fetching recipe details:', error);
      }
    }

    async function fetchRecipeReviews() {
      try {
        const response = await fetch(`http://localhost:8080/recipe-review/list/${recipeId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken')
          }
        });
        if (response.ok) {
          const reviewdata = await response.json();
          setRecipeReviews(reviewdata);
        } else {
          throw new Error('Failed to fetch recipe reviews');
        }
      } catch (error) {
        console.error('Error fetching recipe reviews:', error);
      }
    }

    if (recipeId) {
      fetchRecipeDetails();
      fetchRecipeReviews();
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
          <p><strong>레시피 설명</strong> {recipeDetails.foodInformation.text}</p>
          <p><strong>조리 시간 </strong> {recipeDetails.foodInformation.cookingTime} minutes</p>
          <p><strong>양 </strong> {recipeDetails.foodInformation.serving}</p>
        </div>

        <div>
          <h3>재료</h3>
          <ul>
            {recipeDetails.ingredients.map((ingredient, index) => (
              <li key={ingredient.ingredientId || index}>
                {ingredient.ingredientName} - {ingredient.amount}
              </li>
            ))}
          </ul>
        </div>

        <div>
          <h3>순서</h3>
          <ol>
            {recipeDetails.cookSteps.map((step, index) => (
              <li key={step.cookStepId || index}>
                <strong>Step {step.stepNumber}</strong> {step.text}
                {step.stepImgUrl && <img src={step.stepImgUrl} alt={`Step ${step.stepNumber}`} style={{ width: '100%', maxHeight: '200px' }} />}
              </li>
            ))}
          </ol>
        </div>

        <div>
          <LikeButton recipeId={recipeId} />
        </div>

        <div>
          <ScrapButton recipeId={recipeId} />
        </div>

        <div>
          <h3>리뷰</h3>
          <ul>
            {recipeReviews.map((review, index) => (
              <li key={index}>
                <p><strong>{review.nickname}</strong> (평점 : {review.grade})</p>
                <p>{review.text}</p>
              </li>
            ))}
          </ul>
        </div>

        <div>
          <CreateRecipeReview recipeId={recipeId} />
        </div>
      </div>
    </div>
  );
}

export default RecipeDetails;
