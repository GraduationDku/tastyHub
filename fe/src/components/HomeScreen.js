import React from 'react';

function HomeScreen({ setScreen }) {
  return (
    <div>
      <h1>TastyHub</h1>
      <button onClick={() => alert('Navigating to main screen...')}>비회원</button>
      <br></br>
      <button onClick={() => setScreen('login')}> 회원</button>
    </div>
  );
}

export default HomeScreen;
