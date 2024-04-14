import React, { useState } from 'react';
import HomeScreen from './components/HomeScreen';
import Login from './components/Login';
import Signup from './components/Signup';
import Village from './components/Village';

function App() {
  const [screen, setScreen] = useState('home'); // 'home', 'login', 'signup'

  return (
    <div>
      {screen === 'home' && <HomeScreen setScreen={setScreen} />}
      {screen === 'login' && <Login setScreen={setScreen} />}
      {screen === 'signup' && <Signup setScreen={setScreen} />}
      {screen === 'village' && <Village setScreen={setScreen}/>}
    </div>
  );
}


export default App;
