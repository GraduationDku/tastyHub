import React,{ useState } from "react";

function CreatePost(){
  const [formData, setFormData] = useState({
    title: '',
    text: ''
  })

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name] : e.target.value
    });
  };
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    try{
      const response = await fetch('http://localhost:8080/post/create', {
        method: 'POST',
        headers: {
          'content-Type': 'application/json',
        },
        body : JSON.stringify({
          "text" : formData.text,
          "title" : formData.title
        })
      });
      if(response.ok){
        alert('Recipe created successfully!');
      }else{
        throw new Error('Failed to create Post');
      }
  } catch (error){
    console.error('Error:', error);
    alert('Error Creating Post');
  }
}

return (
  <>
  <form onSubmit={handleSubmit}>
    제목 :
    <input type = "text" name = "title" value={formData.title} onChange={handleChange} />
    내용 :
    <input type="text" name ="text" value={formData.text} onChange = {handleChange} />
    <button onClick={handleSubmit}>작성하기</button>
  </form>
  </>
)}

export default CreatePost;