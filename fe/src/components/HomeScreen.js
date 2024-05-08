import React from 'react';
import '../css/HomeScreen.css'

function HomeScreen({ setScreen }) {
  return (
    <div className="homescreen">
      <h1>TastyHub</h1>
      <img src={require('../css/img/logo.png')} alt="TastyHub Logo" />
      <button className='top' onClick={() => alert('Navigating to main screen...')}>비회원</button>
      <br></br>
      <br/>
      <button className='bottom' onClick={() => setScreen('login')}> 회원</button>
    </div>
  );
}


export default HomeScreen;
