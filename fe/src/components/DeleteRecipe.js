import React from 'react';

const DeleteRecipe = ({ recipeId }) => {
    const deleteRecipe = async () => {
        try {
            const response = await fetch(`http://localhost:8080/recipe/delete/${recipeId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                }
            });
            if (response.ok) {
                const authorization = response.headers.get('Authorization');
                const refreshToken = response.headers.get('Refresh');
                localStorage.setItem('accessToken', authorization);
                localStorage.setItem('refreshToken', refreshToken);
                alert('Recipe deleted successfully!');
                // You can call a callback here if needed
            } else {
                throw new Error('Failed to delete recipe');
            }
        } catch (error) {
            console.error('Error deleting recipe:', error);
            alert('Error deleting recipe');
        }
    };

    return (
        <button onClick={deleteRecipe}>
            레시피 삭제하기
        </button>
    );
};

export default DeleteRecipe;
