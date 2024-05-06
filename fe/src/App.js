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


function App() {
  const [screen, setScreen] = useState('home'); //초기값은 home
  const [selectedRecipeId, setSelectedRecipeId] = useState(null);

  const handleRecipeSelect = (id) =>{
    selectedRecipeId(id);
    setScreen('recipeDetails');
  }

  return (
    <div>
      <Navbar setScreen={setScreen} />
      {screen === 'home' && <HomeScreen setScreen={setScreen} />}
      {screen === 'login' && <Login setScreen={setScreen} />}
      {screen === 'signup' && <Signup setScreen={setScreen} />}
      {screen === 'village' && <Village setScreen={setScreen}/>}
      {screen === 'main' && <MainScreen onRecipeSelect={setScreen}/>}
      {screen === 'recipeDetails' && <Recipedetails setScreen={setScreen}/>}
      {screen === 'recipe' && <Recipe onRecipeSelect={handleRecipeSelect} setScreen={setScreen}/>}
      {screen === 'create' && <CreateRecipe setScreen={setScreen}/>}      
    </div>
  );
}


export default App;
