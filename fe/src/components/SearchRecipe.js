import React, { useState, useEffect } from 'react';

const SearchRecipe = ({ searchTerm }) => {

  useEffect(() => {
    console.log('Updated searchTerm:', searchTerm);
    fetchRecipes();
  }, [searchTerm]);
  
  const [recipes, setRecipes] = useState([]);
  console.log(searchTerm);
  const fetchRecipes = async () => {
    
    if (!searchTerm) {
      setRecipes([]); // 검색어가 없으면 결과를 비움
      return;
    }

    const url = `http://localhost:8080/recipe/search/${searchTerm}`;
    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      const data = await response.json();
      if (response.ok) {
        setRecipes(data);
        
      } else {
        throw new Error('Failed to fetch recipes');
      }
    } catch (error) {
      console.error('Error fetching recipes:', error);
      setRecipes([]);
    }
  };

  useEffect(() => {
    fetchRecipes();
  }, [searchTerm]);

  return (
    <div>
      <h1>검색 결과</h1>
      {recipes.length > 0 ? (
        <ul>
          {recipes.map((recipe) => (
            <li key={recipe.foodId}>
              <h2>{recipe.foodName}</h2>
              <img src={recipe.foodImgUrl} alt={recipe.foodName} />
              <div>
                <p>{recipe.foodInformationDto.text}</p>
                <p>Cooking Time: {recipe.foodInformationDto.cookingTime} minutes</p>
                <p>Servings: {recipe.foodInformationDto.serving}</p>
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p>No recipes found</p>
      )}
    </div>
  );
};

export default SearchRecipe;
