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
import SearchRecipe from './components/SearchRecipe';



function App() {
  const [screen, setScreen] = useState('home'); // 초기 화면 설정
  const [selectedRecipeId, setSelectedRecipeId] = useState(null); // 선택된 레시피 ID 관리

  // 레시피 선택 핸들러
  const handleRecipeSelect = (id) => {
    setSelectedRecipeId(id); // 선택된 레시피 ID 설정
    setScreen('recipeDetails'); // 화면을 레시피 상세 정보로 변경
  }

  return (
    <div>
      {screen !== 'home' && <Navbar setScreen={setScreen} />}
      {/* {screen !== 'home' && screen !== 'login' && screen !== 'signup' && <Navbar setScreen={setScreen} />} */}
      {screen === 'home' && <HomeScreen setScreen={setScreen} />}
      {screen === 'login' && <Login setScreen={setScreen} />}
      {screen === 'signup' && <Signup setScreen={setScreen} />}
      {screen === 'village' && <Village setScreen={setScreen} />}
      {screen === 'main' && <MainScreen onRecipeSelect={handleRecipeSelect} />}
      {screen === 'recipeDetails' && <Recipedetails recipeId={selectedRecipeId} setScreen={setScreen} />}
      {screen === 'recipe' && <Recipe onRecipeSelect={handleRecipeSelect} setScreen={setScreen} />}
      {screen === 'create' && <CreateRecipe setScreen={setScreen} />}
      {screen === 'findUsername' && <FindUsername setScreen={setScreen}/>}
      {screen === 'search' && <SearchRecipe setScreen={setScreen}/>}
    
    </div>
  );
}

export default App;
