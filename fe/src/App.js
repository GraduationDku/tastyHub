import React, { useState } from 'react';
import HomeScreen from './components/HomeScreen';
import Login from './components/Login';
import Signup from './components/Signup';
import Village from './components/Village';
import Navbar from './components/Navbar';
import Recipedetails from './components/Recipedetails';
import MainScreen from './components/MainScreen';
import Recipe from './components/Recipe';
import CreateRecipe from './components/CreateRecipe';
import FindUsername from './components/FindUsername';
import MainChat from './components/MainChat';
import Post from './components/Post';
import './App.css';

function App() {
  const [screen, setScreen] = useState('home');
  const [selectedRecipeId, setSelectedRecipeId] = useState(null);
  const [searchResults, setSearchResults] = useState([]);

  const handleRecipeSelect = (id) => {
    setSelectedRecipeId(id);
    setScreen('recipeDetails');
  };

  const handleSearchComplete = (results) => {
    setSearchResults(results);
    setScreen('searchResults');
  };

  return (
    <div>
      {screen !== 'home' && <Navbar setScreen={setScreen} onSearchComplete={handleSearchComplete} />}
      {screen === 'home' && <HomeScreen setScreen={setScreen} />}
      {screen === 'login' && <Login setScreen={setScreen} />}
      {screen === 'signup' && <Signup setScreen={setScreen} />}
      {screen === 'village' && <Village setScreen={setScreen} />}
      {screen === 'main' && <MainScreen onRecipeSelect={handleRecipeSelect} />}
      {screen === 'recipeDetails' && <Recipedetails recipeId={selectedRecipeId} setScreen={setScreen} />}
      {screen === 'recipe' && <Recipe onRecipeSelect={handleRecipeSelect} setScreen={setScreen} />}
      {screen === 'create' && <CreateRecipe setScreen={setScreen} />}
      {screen === 'findUsername' && <FindUsername setScreen={setScreen} />}
      {screen === 'mainchat' && <MainChat setScreen = {setScreen}/>}
      {screen === 'post' && <Post setScreen={setScreen} />}
      
      {screen === 'searchResults' && (
        <div className="search-results">
          <h1>검색 결과</h1>
          <div className='box'>
            {searchResults.length > 0 ? (
              <ul>
                {searchResults.map((recipe) => (
                  <li key={recipe.foodId} onClick={() => handleRecipeSelect(recipe.foodId)}>
                    <h2>{recipe.foodName}</h2>
                    <img src={recipe.foodImgUrl} alt={recipe.foodName} />
                    <div>
                      <p>{recipe.foodInformationDto.text}</p>
                      <p>조리 시간 : {recipe.foodInformationDto.cookingTime} 분</p>
                      <p>{recipe.foodInformationDto.serving}인분</p>
                    </div>
                  </li>
                ))}
              </ul>
            ) : (
              <p>No recipes found</p>
            )}
            <button onClick={() => setScreen('main')}>Go Back</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
