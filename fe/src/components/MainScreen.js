import React, { useState, useEffect } from 'react';

function MainScreen() {
  const [recipes, setRecipes] = useState([]);

  useEffect(() => {
    async function WeeklyRecipes() {
      try {
        const response = await fetch('http://localhost:8080/recipe/popular');
        const data = await response.json({
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            foodId,
            foodName,
            foodImgUrl,
            foodInformation
          }),
        });
        setRecipes(data);
      } catch (error) {
        console.error('Error fetching weekly recipes:', error);
      }
    }

    WeeklyRecipes();
  }, []);

  return (
    <div>
      <h1>주간 인기 레시피</h1>
      <ul>
        {recipes.map(recipe => (
          <li key={recipe.id} onClick={() => alert('레시피 상세 정보')}>
            {recipe.title}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default MainScreen;
