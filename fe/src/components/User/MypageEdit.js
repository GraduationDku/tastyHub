import React, { useState, useEffect } from 'react';
import '../../../src/css/MypageEdit.css';

function MypageEdit() {
  // 상태 관리
  const [userImg, setUserImg] = useState('');
  const [nickname, setNickname] = useState('');
  const [beforePassword, setBeforePassword] = useState('');
  const [resetPassword, setResetPassword] = useState('');
  const [location, setLocation] = useState({ lat: null, lng: null });
  const [mapLoaded, setMapLoaded] = useState(false);
  const [currentStep, setCurrentStep] = useState(1); // 현재 단계 상태

  // 단계 변경 핸들러
  const nextStep = () => {
    setCurrentStep((prevStep) => prevStep + 1);
  };

  const prevStep = () => {
    setCurrentStep((prevStep) => prevStep - 1);
  };

  // 프로필 사진 핸들러
  const handleFileChange = (e) => {
    setUserImg(e.target.files[0]);
  };

  const handleProfilePicSubmit = async (e) => {
    e.preventDefault();
    // 프로필 사진 수정 API 호출
  };

  // 사용자 정보 수정 핸들러
  const handleUserInfoSubmit = async (e) => {
    e.preventDefault();
    // 사용자 정보 수정 API 호출
  };

  // 위치 정보 가져오기 및 네이버 맵 초기화
  useEffect(() => {
    const script = document.createElement('script');
    script.src = "https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=tyevkp01u2&submodules=geocoder";
    script.async = true;
    script.onload = () => setMapLoaded(true);
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
      const map = new window.naver.maps.Map('map', {
        center: new window.naver.maps.LatLng(location.lat, location.lng),
        zoom: 10,
      });
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
          setLocation({ lat: latitude, lng: longitude });
        },
        (error) => alert("위치 정보를 가져오는데 실패했습니다. 에러 메시지: " + error.message),
        { timeout: 10000 }
      );
    } else {
      alert("이 브라우저에서는 위치 서비스를 지원하지 않습니다.");
    }
  };

  const handleLocationSubmit = async () => {
    // 위치 정보 수정 API 호출
  };

  return (
    <body>
      <br/><br/>
      <div className='mypageedit'>
      {/* 1단계: 프로필 사진 수정 */}
      {currentStep === 1 && (
        <div>
          <h2 className='mypageedittitle'>프로필 사진 수정</h2>
          <form onSubmit={handleProfilePicSubmit}>
            <input type="file" onChange={handleFileChange} />
            <button type="submit">수정</button>
          </form>
          <button type="button" onClick={nextStep}>다음</button>
        </div>
      )}

      {/* 2단계: 사용자 정보 수정 */}
      {currentStep === 2 && (
        <div>
          <h2 className='mypageedittitle'>사용자 정보 수정</h2>
          <form onSubmit={handleUserInfoSubmit}>
            <input type="text" placeholder="새 닉네임" value={nickname} onChange={(e) => setNickname(e.target.value)} /><br />
            <input type="password" placeholder="현재 비밀번호" value={beforePassword} onChange={(e) => setBeforePassword(e.target.value)} /><br />
            <input type="password" placeholder="새 비밀번호" value={resetPassword} onChange={(e) => setResetPassword(e.target.value)} /><br />
            <button type="submit">수정</button>
          </form>
          <button type="button" onClick={prevStep}>이전</button>
          <button type="button" onClick={nextStep}>다음</button>
        </div>
      )}

      {/* 3단계: 위치 수정 */}
      {currentStep === 3 && (
        <div className="village">
          <h2 className='mypageedittitle'>동네 수정</h2>
          <button onClick={getLocation}>현재 위치 가져오기</button>
          {location.lat && location.lng && (
            <>
              <div
              id="map" 
              style={{
                 width: "100%", 
                 height: "400px",
                 borderRadius: "20px", // 모서리를 둥글게 만듦
                 boxShadow: "0 4px 8px rgba(0, 0, 0, 0.3)", // 그림자 설정
                 }}></div>
              <button onClick={handleLocationSubmit}>위치 수정</button>
            </>
          )}
          <button type="button" onClick={prevStep}>이전</button>
        </div>
      )}
     </div> 
    </body>
  );
}

export default MypageEdit;
