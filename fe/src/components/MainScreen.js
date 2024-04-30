import React, { useState, useEffect } from 'react';

function MainScreen() {
  const [recipes, setRecipes] = useState([]);
  const [selectedRecipe, setSelectedRecipe] = useState(null);

  useEffect(() => {
    async function fetchWeeklyRecipes() {
      try {
        const response = await fetch('http://localhost:8080/recipe/popular', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });
        if (response.ok) {
          const data = await response.json();
          setRecipes(data);
        } else {
          throw new Error('Failed to fetch recipes');
        }
      } catch (error) {
        console.error('Error fetching weekly recipes:', error);
      }
    }

    fetchWeeklyRecipes();
  }, []);

  const handleRecipeClick = (recipe) => {
    setSelectedRecipe(recipe);
  };

  return (
    <div>
      <h1>주간 인기 레시피</h1>
      <ul>
        {recipes.map((recipe) => (
          <li key={recipe.foodId} onClick={() => handleRecipeClick(recipe)}>
            {recipe.title} - {recipe.foodName}
          </li>
        ))}
      </ul>
      {selectedRecipe && (
        <div>
          <h2>{selectedRecipe.title}</h2>
          <img src={selectedRecipe.foodImgUrl} alt={selectedRecipe.foodName} style={{ width: '300px' }} />
          <p>{selectedRecipe.foodInformation}</p>
        </div>
      )}
    </div>
  );
}

export default MainScreen;
