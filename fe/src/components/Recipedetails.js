import React, { useState, useEffect } from 'react';

function RecipeDetails({ recipeId }) {
  const [recipeDetails, setRecipeDetails] = useState(null);

  useEffect(() => {
    async function fetchRecipeDetails() {
      try {
        const response = await fetch(`http://localhost:8080/recipe/details/${recipeId}`);
        const data = await response.json({
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            recipeId,
            foodName,
            foodImgUrl,
            foodInformation,
            foodInformationId,
            ingredients,
            cooksteps
          }),
        });
        setRecipeDetails(data);
      } catch (error) {
        console.error('Error fetching recipe details:', error);
      }
    }

    if (recipeId) {
      fetchRecipeDetails();
    }
  }, [recipeId]);

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
              {recipeDetails.cooksteps.map((step, index) => (
                <li key={index}>{step}</li>
              ))}
            </ol>
          </div>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
}

export default RecipeDetails;
