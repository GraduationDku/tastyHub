import React, { useState, useEffect } from 'react';
import '../../css/MypageEdit.css';

function MypageEdit() {
  // 상태 관리
  const [userImg, setUserImg] = useState('');
  const [nickname, setNickname] = useState('');
  const [beforePassword, setBeforePassword] = useState('');
  const [resetPassword, setResetPassword] = useState('');
  const [location, setLocation] = useState({ lat: null, lng: null });
  const [mapLoaded, setMapLoaded] = useState(false);

  // 프로필 사진 핸들러
  const handleFileChange = (e) => {
    setUserImg(e.target.files[0]);
  };

  const handleProfilePicSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('userImg', userImg);

    try {
      const response = await fetch('http://localhost:8080/user/modify/profile-pic', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
      },
        body: userImg,
      });

      if (response.ok) {
        console.log('프로필 사진이 성공적으로 수정되었습니다.');
      } else {
        console.error('프로필 사진 수정에 실패했습니다:', response.status, await response.text());
      }
    } catch (error) {
      console.error('서버 요청 중 오류 발생:', error);
    }
  };

  // 사용자 정보 수정 핸들러
  const handleUserInfoSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/user/modify/information', {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
        body: JSON.stringify({
          nickname,
          beforePassword,
          resetPassword,
        }),
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

  // 위치 정보 가져오기 및 네이버 맵 초기화
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
          'Authorization' : localStorage.getItem('accessToken')
          
        },
        body: JSON.stringify({
          lat: location.lat,
          lng: location.lng,
        }),
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

  return (
    <div>
      <h1>마이페이지 수정</h1>
      
      <form onSubmit={handleProfilePicSubmit}>
        <h2>프로필 사진 수정</h2>
        <input type="file" onChange={handleFileChange} />
        <button type="submit">수정</button>
      </form>
      
      <form onSubmit={handleUserInfoSubmit}>
        <h2>사용자 정보 수정</h2>
        <input type="text" placeholder="새 닉네임" value={nickname} onChange={(e) => setNickname(e.target.value)} /><br/>
        <input type="password" placeholder="현재 비밀번호" value={beforePassword} onChange={(e) => setBeforePassword(e.target.value)} /><br/>
        <input type="password" placeholder="새 비밀번호" value={resetPassword} onChange={(e) => setResetPassword(e.target.value)} /><br/>
        <button type="submit">수정</button>
      </form>
      
      <div className="village">
        <h2>동네 수정</h2>
        <button onClick={getLocation}>현재 위치 가져오기</button>
        {location.lat && location.lng && (
          <>
            <p>위도: {location.lat}, 경도: {location.lng}</p>
            <div className="map" id="map" style={{ width: "100%", height: "400px" }}></div>
            <button onClick={handleLocationSubmit}>위치 수정</button>
          </>
        )}
      </div>
    </div>
  );
}

export default MypageEdit;
