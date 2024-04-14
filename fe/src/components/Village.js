import React, { useState, useEffect } from 'react';

function Village() {
  const [location, setLocation] = useState({ lat: null, lng: null });
  const [addressTownName, setAddress] = useState('');
  const [mapLoaded, setMapLoaded] = useState(false);

  useEffect(() => {
    const script = document.createElement('script');
    script.src = "https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=tyevkp01u2";
    script.async = true;
    script.onload = () => setMapLoaded(true);
    document.head.appendChild(script);
  }, []);

  const getAddressFromNaver = async (lat, lng) => {
    const clientId = 'tyevkp01u2';
    const clientSecret = 'hn1A68yG8Ln4HisR4p|JzmRVJZK2gPIWM31PjLxJ';
    const url = `https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=${lng},${lat}&orders=addr&output=json`;

    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'X-NCP-APIGW-API-KEY-ID': clientId,
          'X-NCP-APIGW-API-KEY': clientSecret,
        }
      });

      if (response.ok) {
        const data = await response.json();
        const addressData = data.results[0]?.region;
        const addressTownName = `${addressData.area1.name} ${addressData.area2.name} ${addressData.area3.name}`;
        setAddress(addressTownName);
      } else {
        console.error("서버로부터 에러 응답을 받았습니다.");
      }
    } catch (error) {
      console.error("주소 가져오기 중 오류 발생:", error);
    }
  };

  useEffect(() => {
    const checkNaver = setInterval(() => {
      if (mapLoaded && location.lat && location.lng) {
        getAddressFromNaver(location.lat, location.lng);
        initializeNaverMap();
        clearInterval(checkNaver);
      }
    }, 100);

    return () => clearInterval(checkNaver);
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
      navigator.geolocation.getCurrentPosition((position) => {
        const { lat, lng } = position.coords;
        setLocation({ lat, lng });
      }, (error) => {
        console.error("Error Code = " + error.code + " - " + error.message);
      });
    } else {
      alert("위치를 불러올 수 없습니다.");
    }
  };

  const handleConfirmLocation = () => {
    console.log("레시피 화면으로 이동");
  };

  return (
    <div>
      <h2>나의 동네가 맞나요?</h2>
      <button onClick={getLocation}>현재 위치 가져오기</button>
      {location.lat && location.lng && (
        <>
          <p>위도: {location.lat}, 경도: {location.lng}</p>
          <p>주소: {addressTownName}</p>
          <div id="map" style={{ width: "100%", height: "400px" }}></div>
          <button onClick={handleConfirmLocation}>네, 맞아요</button>
          <button onClick={getLocation}>아니에요</button>
        </>
      )}
    </div>
  );
}

export default Village;
