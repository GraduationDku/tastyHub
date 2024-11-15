import React, { useState, useEffect, useRef } from 'react';
import CreateRecipeReview from '../Review/CreateRecipeReview';
import LikeButton from '../Like/Like';
import ScrapButton from '../Scrap/Scrap';
import YouTube from 'react-youtube'; // YouTube 라이브러리
import '../../css/RecipeDetail.css';

function RecipeDetails({ recipeId }) {
  const [recipeDetails, setRecipeDetails] = useState(null);
  const [recipeReviews, setRecipeReviews] = useState([]);
  const videoRef = useRef(null); // 비디오 컨트롤을 위한 ref
  const youtubePlayerRef = useRef(null); // YouTube 플레이어 ref

  useEffect(() => {
    async function fetchRecipeDetails() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe/detail/${recipeId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken'),
          },
        });
        if (response.ok) {
          const data = await response.json();
          setRecipeDetails(data);
        } else {
          throw new Error('Failed to fetch recipe details');
        }
      } catch (error) {
        console.error('Error fetching recipe details:', error);
      }
    }

    async function fetchRecipeReviews() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe-review/list/${recipeId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken'),
          },
        });
        if (response.ok) {
          const reviewdata = await response.json();
          setRecipeReviews(reviewdata);
        } else {
          throw new Error('Failed to fetch recipe reviews');
        }
      } catch (error) {
        console.error('Error fetching recipe reviews:', error);
      }
    }

    if (recipeId) {
      fetchRecipeDetails();
      fetchRecipeReviews();
    }
  }, [recipeId]);

  const handleTimelineClick = (timeline) => {
    if (!timeline) return;

    const timeParts = timeline.split(':').map(Number);
    const seconds = timeParts.reduce((acc, val) => acc * 60 + val, 0); // 시간 문자열을 초 단위로 변환

    if (recipeDetails.recipeType === 'video' && videoRef.current) {
      videoRef.current.currentTime = seconds; // 비디오 현재 시간 설정
      videoRef.current.play(); // 재생
    }

    if (recipeDetails.recipeType === 'youtube' && youtubePlayerRef.current) {
      youtubePlayerRef.current.internalPlayer.seekTo(seconds, true); // 유튜브 플레이어 시간 이동
    }
  };

  if (!recipeDetails) {
    return <p>Loading...</p>;
  }

  return (
    <div className="recipedetails">
      <h1>{recipeDetails.foodName}</h1>
      <div className="box">
        {/* 대표 사진 */}
        <img src={recipeDetails.foodImgUrl} alt={recipeDetails.foodName} />

        {/* 비디오 */}
        {recipeDetails.recipeType === 'video' && recipeDetails.foodVideoUrl && (
          <video
            ref={videoRef}
            src={recipeDetails.foodVideoUrl}
            controls
            style={{ width: '100%', maxHeight: '500px' }}
          />
        )}

        {/* 유튜브 */}
        {recipeDetails.recipeType === 'youtube' && recipeDetails.foodVideoUrl && (
          <YouTube
            videoId={new URL(recipeDetails.foodVideoUrl).searchParams.get('v')} // 유튜브 링크에서 videoId 추출
            opts={{ width: '100%', height: '500px', playerVars: { controls: 1 } }}
            onReady={(e) => (youtubePlayerRef.current = e.target)}
          />
        )}

        {/* 요리 설명 */}
        <div>
          <p className="dis">
            <strong>레시피 설명<br /><br /></strong> {recipeDetails.foodInformation.content}
          </p>
          <p className="time">
            <strong>조리 시간 </strong> {recipeDetails.foodInformation.cookingTime}분
          </p>
          <p className="amount">
            <strong>양 </strong> {recipeDetails.foodInformation.serving}
          </p>
        </div>

        {/* 재료 */}
        <div>
          <h3>재료</h3>
          <ul className="recipedetail">
            {recipeDetails.ingredients.map((ingredient, index) => (
              <li className="recipedetail" key={ingredient.ingredientId || index}>
                {ingredient.ingredientName} - {ingredient.amount}
              </li>
            ))}
          </ul>
        </div>

        {/* 순서 */}
        <div>
          <h3>순서</h3>
          <ol className="recipedetail">
            {recipeDetails.cookSteps.map((step, index) => (
              <li
                className="recipedetail"
                key={step.stepNumber || index}
                style={{
                  cursor:
                    recipeDetails.recipeType === 'photo' ? 'default' : 'pointer',
                }}
                onClick={() => handleTimelineClick(step.timeLine)}
              >
                {step.content}
                <br />
                {recipeDetails.recipeType === 'photo' && step.cookStepImg && (
                  <img
                    src={step.cookStepImg}
                    alt={`Step ${step.stepNumber}`}
                    style={{ width: '100%', maxHeight: '200px' }}
                  />
                )}
              </li>
            ))}
          </ol>
        </div>

        {/* 좋아요 & 스크랩 버튼 */}
        <p style={{ display: 'flex', gap: '20px' }}>
          <LikeButton className="recipedetail" recipeId={recipeId} />
          <ScrapButton recipeId={recipeId} />
        </p>

        {/* 리뷰 */}
        <div className="review">
          <div>
            <ul>
              {recipeReviews.map((review, index) => (
                <li key={index}>
                  <p>
                    <strong>{review.nickname}</strong> (평점 : {review.grade})
                  </p>
                  <p>{review.content}</p>
                </li>
              ))}
            </ul>
          </div>
          <div>
            <CreateRecipeReview recipeId={recipeId} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default RecipeDetails;
