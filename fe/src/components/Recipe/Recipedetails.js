import React, { useState, useEffect } from 'react';
import CreateRecipeReview from '../Review/CreateRecipeReview';
import LikeButton from '../Like/Like'; // Ensure you import the LikeButton component correctly
import ScrapButton from '../Scrap/Scrap'; // Import the ScrapButton component
import '../../css/RecipeDetail.css';

function RecipeDetails({ recipeId }) {
  const [recipeDetails, setRecipeDetails] = useState(null);
  const [recipeReviews, setRecipeReviews] = useState([]);

  useEffect(() => {
    async function fetchRecipeDetails() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe/detail/${recipeId}`, {
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

    async function fetchRecipeReviews() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe-review/list/${recipeId}`, {
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
          <p className='dis'><strong>레시피 설명<br/><br/></strong> {recipeDetails.foodInformation.content}</p>
          <p className='time'><strong>조리 시간 </strong> {recipeDetails.foodInformation.cookingTime}분</p>
          <p className='amount'><strong>양 </strong> {recipeDetails.foodInformation.serving}</p>
        </div>

        <div>
          <h3>재료</h3>
          <ul className='recipedetail'>
            {recipeDetails.ingredients.map((ingredient, index) => (
              <li className='recipedetail' key={ingredient.ingredientId || index} >
                {ingredient.ingredientName} - {ingredient.amount}
              </li>
            ))}
          </ul>
        </div>

        <div>
          <h3>순서</h3>
          <ol className='recipedetail'>
            {recipeDetails.cookSteps.map((step, index) => (
              <li className='recipedetail' key={step.cookStepId || index}>
                 {step.content}<br/><br/>
                {step.stepImgUrl && <img src={step.stepImgUrl} alt={`Step ${step.stepNumber}`} style={{ width: '100%', maxHeight: '200px' }} />}
              </li>
            ))}
          </ol>
        </div>

        <p style={{ display: 'flex', gap: '20px' }}>
          <LikeButton className='recipedetail'recipeId={recipeId} />
          <ScrapButton recipeId={recipeId} />
        </p>
        <br/><br/><br/>
        <div className='review'>
          <div>
            <ul>
              {recipeReviews.map((review, index) => (
                <li key={index}>
                  <p><strong>{review.nickname}</strong> (평점 : {review.grade})</p>
                  <p>{review.content}</p>
                </li>
              ))}
            </ul>
          </div>
          <br/><br/><br/>
          <div>
            <CreateRecipeReview recipeId={recipeId} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default RecipeDetails;
