import React, { useState, useEffect } from 'react';
import '../css/MainScreen.css';

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
          console.log('Received data:', data); // 추가된 로그
          if (Array.isArray(data.content)) {
            setRecipes(data.content);
          } else {
            console.error('Data.content is not an array', data.content);
          }
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
    console.log('Selected recipe:', recipe); // 추가된 로그
  };

  return (
    <div className='mainscreen'>
      <h1>주간 인기 레시피</h1>
      <div className='box'>
      <ul>
        {recipes.map((recipe) => (
          <li key={recipe.foodId} onClick={() => handleRecipeClick(recipe)}>
            {recipe.title}  {recipe.foodName}
          </li>
        ))}
      </ul>
      </div>
      <div className='box2'>
      {selectedRecipe && (
        <div>
          <h2>{selectedRecipe.title}</h2>
          <img src={selectedRecipe.foodImgUrl} alt={selectedRecipe.foodName} style={{ width: '300px' }} />
          {selectedRecipe.foodInformationDto ? (
            <>
              <p>{selectedRecipe.foodInformationDto.text || 'No description available'}</p>
              <p>Cooking Time: {selectedRecipe.foodInformationDto.cookingTime || 'N/A'} minutes</p>
              <p>Serving: {selectedRecipe.foodInformationDto.serving || 'N/A'}인분</p>
            </>
          ) : (
            <p>No additional food information available.</p>
          )}
        </div>
      )}
    </div>
    </div>
  );
}

export default MainScreen;
