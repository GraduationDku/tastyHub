import React, { useState, useEffect } from 'react';
import '../css/MypageEdit.css';

function MypageEdit() {
  const [userImg, setUserImg] = useState(null); // 초기값을 null로 설정
  const [nickname, setNickname] = useState('');
  const [beforePassword, setBeforePassword] = useState('');
  const [resetPassword, setResetPassword] = useState('');
  const [location, setLocation] = useState({ lat: null, lng: null });
  const [mapLoaded, setMapLoaded] = useState(false);
  const [isPasswordMatch, setIsPasswordMatch] = useState(false);
  const [isPasswordEmpty, setIsPasswordEmpty] = useState(true);
  const [isNicknameEntered, setIsNicknameEntered] = useState(false);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setUserImg(file);
  };

  const handleUserInfoSubmit = async (e) => {
    e.preventDefault();

    try {
      const data = {
        nickname: nickname
      };
      console.log(data)
      console.log(userImg)
      const formData = new FormData();
      formData.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }));
      formData.append('img', userImg);
      

      const response = await fetch('http://localhost:8080/user/modify/information', {
        method: 'POST',
        headers: {
          'Authorization': localStorage.getItem('accessToken'),
          'Content-Type': 'multipart/form-data'
        },
        body: formData,
      });

      if (response.ok) {
        console.log('사용자 정보가 성공적으로 수정되었습니다.');
      } else {
        console.error('사용자 정보 수정에 실패했습니다:', response.status, await response.text());
      }
    } catch (error) {
      console.error('서버 요청 중 오류 발생:', error);
    }
  };

  useEffect(() => {
    const script = document.createElement('script');
    script.src = "https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=tyevkp01u2&submodules=geocoder";
    script.async = true;
    script.onload = () => {
      console.log("Naver Maps API is loaded.");
      setMapLoaded(true);
    };
    document.head.appendChild(script);

    return () => {
      document.head.removeChild(script);
    };
  }, []);

  useEffect(() => {
    if (mapLoaded && location.lat && location.lng) {
      initializeNaverMap();
    }
  }, [mapLoaded, location]);

  const initializeNaverMap = () => {
    if (window.naver && window.naver.maps) {
      const mapOptions = {
        center: new window.naver.maps.LatLng(location.lat, location.lng),
        zoom: 10,
      };
      const map = new window.naver.maps.Map('map', mapOptions);
      new window.naver.maps.Marker({
        position: new window.naver.maps.LatLng(location.lat, location.lng),
        map: map,
      });
    }
  };

  const getLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          console.log("Location obtained:", latitude, longitude);
          setLocation({ lat: latitude, lng: longitude });
        },
        (error) => {
          console.error("Error obtaining location:", error.message);
          alert("위치 정보를 가져오는데 실패했습니다. 에러 메시지: " + error.message);
        },
        { timeout: 10000 }
      );
    } else {
      alert("이 브라우저에서는 위치 서비스를 지원하지 않습니다.");
    }
  };

  const handleLocationSubmit = async () => {
    console.log(location.lat);
    console.log(location.lng);
    const serverEndpoint = 'http://localhost:8080/village/modify/location';
    try {
      const response = await fetch(serverEndpoint, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
        body: {
          lat: location.lat,
          lng: location.lng,
        },
      });

      if (response.ok) {
        console.log('위치 정보가 성공적으로 수정되었습니다.');
      } else {
        console.error('위치 정보 수정에 실패했습니다:', response.status, await response.text());
      }
    } catch (error) {
      console.error('서버 요청 중 오류 발생:', error);
    }
  };

  useEffect(() => {
    setIsPasswordMatch(beforePassword === resetPassword);
    setIsPasswordEmpty(!beforePassword && !resetPassword);
  }, [beforePassword, resetPassword]);

  useEffect(() => {
    setIsNicknameEntered(!!nickname);
  }, [nickname]);

  return (
    <div className='mypageedit'>
      <div className='box'>
        <form className='user' onSubmit={handleUserInfoSubmit}>
          <h2>사용자 정보 수정</h2>
          <input type="file" onChange={handleFileChange} /><br />
          <input 
            type="text" 
            placeholder="새 닉네임" 
            value={nickname} 
            onChange={(e) => setNickname(e.target.value)} 
          /><br />
          <button 
            type="submit" 
            style={{ backgroundColor: isNicknameEntered ? '#AEC7A1' : 'gray' }} 
            disabled={!isNicknameEntered}
          >
            수정하기
          </button>
        </form>
      
        <div className='findlocation'>
          <h2>동네 수정</h2>
          <button onClick={getLocation}>현재 위치 가져오기</button>
          {location.lat && location.lng && (
            <>
              <p>위도: {location.lat}, 경도: {location.lng}</p>
              <div className="map" id="map" style={{ width: "700px", height: "400px", borderRadius:"20px" }}></div>
              <br />
              <button onClick={handleLocationSubmit}>위치 수정하기</button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export default MypageEdit;
