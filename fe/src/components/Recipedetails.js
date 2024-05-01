import React, { useState, useEffect } from 'react';

function RecipeDetails({ recipeId }) {
  const [recipeDetails, setRecipeDetails] = useState(null);

  useEffect(() => {
    async function fetchRecipeDetails() {
      try {
        // GET 요청은 body를 포함하지 않아야 하며, 요청 옵션을 정확하게 설정합니다.
        const response = await fetch(`http://localhost:8080/recipe/details/${recipeId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });
        if (response.ok) {
          const data = await response.json(); // 응답을 JSON 형식으로 파싱합니다.
          setRecipeDetails(data); // 파싱된 데이터를 상태로 설정합니다.
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
  }, [recipeId]); // recipeId가 변경될 때마다 새로 데이터를 불러옵니다.

  return (
    <div>
      {recipeDetails ? (
        <div>
          <h1>{recipeDetails.foodName}</h1>
          <img src={recipeDetails.foodImgUrl} alt={recipeDetails.foodName} style={{ width: '100%', maxHeight: '400px' }} />
          <p><strong>Description:</strong> {recipeDetails.foodInformation}</p>
          <div>
            <h3>Ingredients:</h3>
            <ul>
              {recipeDetails.ingredients.map((ingredient, index) => (
                <li key={index}>{ingredient}</li>
              ))}
            </ul>
          </div>
          <div>
            <h3>Cooking Steps:</h3>
            <ol>
              {recipeDetails.cookSteps.map((step, index) => (
                <li key={index}>{step}</li>
              ))}
            </ol>
          </div>
        </div>
      ) : (
        <p>Loading...</p> // 데이터 로딩 중임을 표시
      )}
    </div>
  );
}

export default RecipeDetails;
