import React, { useState } from 'react';
import HomeScreen from './components/HomeScreen';
import Login from './components/User/Login';
import Signup from './components/User/Signup';
import Village from './components/Village/Village';
import Navbar from './components/Navbar';
import Recipedetails from './components/Recipe/Recipedetails';
import MainScreen from './components/MainScreen';
import Recipe from './components/Recipe/Recipe';
import CreateRecipe from './components/Recipe/CreateRecipe';
import FindUsername from './components/User/FindUsername';
import MainChat from './components/Chat/MainChat';
import Post from './components/Post/Post';
import CreatePost from './components/Post/CreatePost';
import SendChat from './components/Chat/SendChat';
import './App.css';
import MainMypage from './components/User/MainMypage';
import MypageEdit from './components/User/MypageEdit';
import PostDetails from './components/Post/PostDetails';
import MypageShow from './components/User/MypageShow';

function App() {
  const [screen, setScreen] = useState('home');
  const [isGuest, setIsGuest] = useState(false);
  const [selectedRecipeId, setSelectedRecipeId] = useState(null);
  const [selectedRoomId, setSelectedRoomId] = useState(null);
  const [selectedPostId, setSelectedPostId] = useState(null);
  const [searchResults, setSearchResults] = useState([]);
  const [username, setUsername] = useState('');

  const handleRecipeSelect = (id) => {
    setSelectedRecipeId(id);
    setScreen('recipeDetails');
  };

  const handleSearchComplete = (results) => {
    setSearchResults(results);
    setScreen('searchResults');
  };

  const handleChatroomSelect = (roomId) => {
    setSelectedRoomId(roomId);
    setScreen('sendchat');
  };

  const handlePostSelect = (postId) => {
    setSelectedPostId(postId);
    setScreen('postDetails');
  };

  const handleGuestAccess = () => {
    setIsGuest(true);
    setScreen('main');
  };

  return (
    <div>
      {screen !== 'home' && <Navbar setScreen={setScreen} onSearchComplete={handleSearchComplete} />}
      {screen === 'home' && <HomeScreen setScreen={setScreen} setIsGuest={setIsGuest} handleGuestAccess={handleGuestAccess} />}
      {screen === 'login' && <Login setScreen={setScreen} />}
      {screen === 'signup' && <Signup setScreen={setScreen} />}
      {screen === 'village' && <Village setScreen={setScreen} />}
      {screen === 'main' && <MainScreen onRecipeSelect={handleRecipeSelect} />}
      {screen === 'recipeDetails' && <Recipedetails recipeId={selectedRecipeId} setScreen={setScreen} />}
      {screen === 'recipe' && <Recipe onRecipeSelect={handleRecipeSelect} setScreen={setScreen} />}
      {screen === 'create' && !isGuest && <CreateRecipe setScreen={setScreen} />}
      {screen === 'findUsername' && <FindUsername setScreen={setScreen} />}
      {screen === 'mainchat' && <MainChat setScreen={setScreen} onChatroomSelect={handleChatroomSelect} isGuest={isGuest} />}
      {screen === 'post' && <Post setScreen={setScreen} onPostSelect={handlePostSelect} isGuest={isGuest} />}
      {screen === 'createpost' && !isGuest && <CreatePost setScreen={setScreen} />}
      {screen === 'mypage' && <MainMypage setScreen={setScreen} isGuest={isGuest} />}
      {screen === 'sendchat' && selectedRoomId && !isGuest && <SendChat roomId={selectedRoomId} username={username} />}
      {screen === 'mypageedit' && !isGuest && <MypageEdit setScreen={setScreen} />}
      {screen === 'postDetails' && <PostDetails postId={selectedPostId} setScreen={setScreen} />}
      {screen === 'mypageshow' && !isGuest && <MypageShow setScreen={setScreen} />}
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
