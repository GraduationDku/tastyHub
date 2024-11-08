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
import PageButton from './components/PageButton';

function App() {
  const [screen, setScreen] = useState('home');
  const [isGuest, setIsGuest] = useState(false);
  const [selectedRecipeId, setSelectedRecipeId] = useState(null);
  const [selectedRoomId, setSelectedRoomId] = useState(null);
  const [selectedPostId, setSelectedPostId] = useState(null);
  const [searchResults, setSearchResults] = useState([]);
  const [username, setUsername] = useState('');
  const [page, setPage] = useState(1); 
  const [size, setSize] = useState(5); 
  const [sort, setSort] = useState('date'); 
  const [totalItems, setTotalItems] = useState(0); 
  const [totalPages, setTotalPages] = useState(0); 
  const [searchType, setSearchType] = useState('recipe');

  const handleRecipeSelect = (id) => {
    setSelectedRecipeId(id);
    setScreen('recipeDetails');
  };

  const handleSearchComplete = (results) => {
    setSearchResults(results || []);
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

  const handleUserSelect = (e) => {
    // User selection logic
  };

  const handleSizeChange = (e) => {
    setSize(parseInt(e.target.value, 10) || 5);
    setPage(1); // 페이지를 1로 초기화
  };

  const handleSortChange = (e) => {
    setSort(e.target.value || 'date');
    setPage(1); // 페이지를 1로 초기화
  };

  const handlePageChange = (newPage) => {
    if (newPage < 1) newPage = 1; // 페이지 번호를 1보다 작지 않도록 설정
    if (newPage > totalPages) newPage = totalPages; // 페이지 번호를 전체 페이지 수보다 크지 않도록 설정
    setPage(newPage);
  };

  return (
    <div>
      {screen !== 'home' && screen !== 'sendchat' && screen !== 'login' && screen !== 'signup' && screen !== 'create' && (
        <Navbar setScreen={setScreen} onSearchComplete={handleSearchComplete} />
      )}
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
      {screen === 'sendchat' && selectedRoomId && !isGuest && <SendChat roomId={selectedRoomId} setScreen={setScreen} username={username} />}
      {screen === 'mypageedit' && !isGuest && <MypageEdit setScreen={setScreen} />}
      {screen === 'postDetails' && <PostDetails postId={selectedPostId} setScreen={setScreen} />}
      {screen === 'mypageshow' && !isGuest && <MypageShow setScreen={setScreen} />}
      {screen === 'searchResults' && (
        <div className='searchdiv'>
          <h1>검색 결과</h1>
          <div className='searchdiv2'>
            <div className='searchsort'>
              <select value={sort} onChange={handleSortChange}>
                <option value="date">날짜</option>
                <option value="title">제목</option>
                <option value="nickname">작성자</option>
              </select>

              <select value={size} onChange={handleSizeChange}>
                <option value={5}>5개</option>
                <option value={10}>10개</option>
                <option value={20}>20개</option>
              </select>
            </div>

            {searchResults.length > 0 && searchType === 'recipe' ? (
              <ul>
                {searchResults.map((recipe) => (
                  <li key={recipe.foodId} onClick={() => handleRecipeSelect(recipe.foodId)}>
                    <h2 className='searchrecipetitle'>{recipe.foodName}</h2>
                    <img src={recipe.foodImgUrl} alt={recipe.foodName} className='searchrecipeimg' style={{ width: '340px' }}/>
                    <div>
                      <p>{recipe.foodInformationDto.text}</p>
                      <div className="recipe-info">
                        <p>{recipe.foodInformationDto.cookingTime}분</p>
                        <p>{recipe.foodInformationDto.serving}</p>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            ) : searchType === 'recipe' && <p>No recipes found</p>}

            {searchResults.length > 0 && searchType === 'user' ? (
              <ul>
                {searchResults.map((user) => (
                  <li key={user.userId} onClick={() => handleUserSelect(user.userId)}>
                    <h2>{user.nickname}</h2>
                    <img src={user.userImg} alt={user.nickname}/>
                  </li>
                ))}
              </ul>
            ) : searchType === 'user' && <p>No users found</p>}

            <button onClick={() => setScreen('main')}>이전</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
