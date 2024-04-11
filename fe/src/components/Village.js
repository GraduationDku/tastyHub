import React, { useState, useEffect } from 'react';

function Village() {
  const [location, setLocation] = useState({ lat: null, lng: null });
  const [addressTownName, setAddress] = useState('');

  // 네이버 지도 API로부터 주소 가져오기
  const getAddressFromNaver = async (lat, lng) => {
    const clientId = '';
    const clientSecret = '';
    // 역지오코딩 API 요청 URL
    const url = `https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=${longitude},${latitude}&orders=addr&output=json`;

    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'X-NCP-APIGW-API-KEY-ID': clientId,
          'X-NCP-APIGW-API-KEY': clientSecret,
        },
        body: JSON.stringify({
          lat,
          lng,
          addressTownName
        })
      });

      if (response.ok) {
        const data = await response.json();
        // 주소 구성
        const addressData = data.results[0]?.region;
        const addressTownName = `${addressData.area1.name} ${addressData.area2.name} ${addressData.area3.name}`;
        setAddress(addressTownName); // 상태 업데이트
      } else {
        console.error("서버로부터 에러 응답을 받았습니다.");
      }
    } catch (error) {
      console.error("주소 가져오기 중 오류 발생:", error);
    }
  };

  useEffect(() => {
    if (location.lat && location.lng) {
      getAddressFromNaver(location.lat, location.lng); // 주소 가져오기
      // 네이버 지도 초기화
      const mapOptions = {
        center: new naver.maps.LatLng(location.lat, location.lng),
        zoom: 10,
      };
      const map = new naver.maps.Map('map', mapOptions);
      // 네이버 지도에 마커 표시
      new naver.maps.Marker({
        position: new naver.maps.LatLng(location.lat, location.lng),
        map: map,
      });
    }
  }, [location]);

  // 현재 위치 가져오기
  const getLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        const { lat, lng } = position.coords;
        setLocation({ lat, lng});
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
      {location.lat && location.lng && (
        <>
          <p>위도: {location.lat}, 경도: {location.lng}</p>
          <p>주소: {addressTownName}</p>
          <div id="map" style={{width: "100%", height: "400px"}}></div>
        </>
      )}
    </div> 
  );
}

export default Village;
