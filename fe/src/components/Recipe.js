import React, { useState, useEffect } from 'react';

function Recipe({ onRecipeSelect, onCreate, onEdit }) {
  const [recipes, setRecipes] = useState([]);

  useEffect(() => {
    async function fetchAllRecipes() {
      try {
        const response = await fetch('http://localhost:8080/recipe/list', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        });
        const data = await response.json();
        setRecipes(data);
      } catch (error) {
        console.error('Error fetching all recipes:', error);
      }
    }

    fetchAllRecipes();
  }, []);

  return (
    <div>
      <h1>전체 레시피 조회</h1>
      <button onClick={onCreate}>레시피 작성하기</button>
      <ul>
        {recipes.map(recipe => (
          <li key={recipe.id} onClick={() => onRecipeSelect(recipe.id)}>
            <h3>{recipe.foodName}</h3>
            <img src={recipe.foodImgUrl} alt={recipe.foodName} style={{ width: '100px', height: '100px' }} />
            <p>{recipe.foodInformation}</p>
            <button onClick={(e) => {
              e.stopPropagation(); // Prevents the list item's onClick
              onEdit(recipe.id);
            }}>수정하기</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Recipe;
