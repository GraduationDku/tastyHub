import React, { useState, useEffect } from 'react';
import '../css/Village.css';

function Village({setScreen}) {
  const [location, setLocation] = useState({ lat: null, lng: null });
  const [mapLoaded, setMapLoaded] = useState(false);

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

  const handleConfirmLocation = async () => {
    const serverEndpoint = 'http://localhost:8080/village/location';
    try {
      const response = await fetch(serverEndpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          "lat": location.lat,
          "lng": location.lng,
        })
      });

      if (response.ok) {
        const responseData = await response.json();
        console.log('서버로부터 응답 받음:', responseData);
        
      } else {
        console.error('서버 에러:', response.status, await response.text());
      }
    } catch (error) {
      console.error('서버 요청 중 오류 발생:', error);
    }
  };

  return (
    <div className='village'>
      <div className='box'>
        <h2>나의 동네가 맞나요?</h2>
        <button onClick={getLocation}>현재 위치 가져오기</button>
        {location.lat && location.lng && (
          <>
            <p>위도: {location.lat}, 경도: {location.lng}</p>
            <div className='map' id="map" style={{ width: "100%", height: "400px" }}></div>
            <br/>
            <button onClick={() => setScreen('main')}>맞아요</button>
            <button onClick={getLocation}>아니에요</button>
          </>
        )}
      </div>
    </div>
  );
}

export default Village;
