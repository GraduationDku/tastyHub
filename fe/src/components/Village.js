import React, { useState, useEffect } from 'react';

function Village() {
  const [location, setLocation] = useState({ latitude: null, longitude: null });

  useEffect(() => {
    if (location.latitude && location.longitude) {
      const mapOptions = {
        center: new naver.maps.LatLng(location.latitude, location.longitude),
        zoom: 10,
      };
      const map = new naver.maps.Map('map', mapOptions);

      new naver.maps.Marker({
        position: new naver.maps.LatLng(location.latitude, location.longitude),
        map: map,
      });
    }
  }, [location]);

  const sendLocationToServer = async (latitude, longitude) => {
    try {
      const response = await fetch('/village/location', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
           lat,
           lng
           }),
      });

      if (response.ok) {
        console.log("위치 정보가 성공적으로 서버로 전송되었습니다.");
      } else {
        console.error("서버로부터 에러 응답을 받았습니다.");
      }
    } catch (error) {
      console.error("위치 정보 전송 중 오류 발생:", error);
    }
  };

  const getLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        const { latitude, longitude } = position.coords;
        setLocation({ latitude, longitude });
        // 위치 정보를 서버로 전송
        sendLocationToServer(latitude, longitude);
      }, (error) => {
        console.error("Error Code = " + error.code + " - " + error.message);
      });
    } else {
      alert("Geolocation is not supported by this browser.");
    }
  };

  return (
    <div>
      <h2>나의 동네가 맞나요?</h2>
      <button onClick={getLocation}>현재 위치 가져오기</button>
      {location.latitude && location.longitude && (
        <>
          <p>위도: {location.latitude}, 경도: {location.longitude}</p>
          <div id="map" style={{width: "100%", height: "400px"}}></div>
        </>
      )}
    </div> 
  );
}

export default Village;
