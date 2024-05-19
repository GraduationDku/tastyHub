import React, {useState, useEffect} from "react";
import RecipeDetails from "./Recipedetails";

function PostDetails({ postId}){
  const [postDetails, setPostDetails] = useState(null);

  useEffect(() => {
    async function fetchPostDetails(){
      try {
        const response = await fetch(`http://localhost:8080/post/detail/${postId}`,{
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });
        if (response.ok){
          const data = await response.json();
          setPostDetails(data);
        }else {
          throw new Error ('Failed to fetch post details');
        }
      }catch(error){
        console.error('Error fetching post details:', error);
      }
    }

    if (postId){
      
    }
  })
}