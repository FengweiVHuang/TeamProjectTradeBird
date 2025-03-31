import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";
import axiosApi from "../util/AxiosRequests";

import TopNavigation from "../components/TopNavigation";

function Home() {
    const [inputValue, setInputValue] = useState('');
    const [file, setFile] = useState(null);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axiosApi.post('/home/testpost', {data: inputValue});
            console.log(response);
            alert('Submission successful');
        } catch (error) {
            console.error(error);
            alert('Submission failed');
        }
    };

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleFileUpload = async (event) => {
        event.preventDefault();
        const formData = new FormData();
        formData.append('file', file);
        formData.append('name', 'profile_picture');

        try {
            const response = await axiosApi.postForm('/users/upload_profile_picture', formData);
            console.log(response);
            alert('File upload successful');
        } catch (error) {
            console.error(error);
            alert('File upload failed');
        }
    };

    const handleLogout = async () => {
        try {
            const response = await axiosApi.get('/users/logout');
            console.log(response);
            alert('Logout successful');
            navigate('/v2/');
        } catch (error) {
            console.error(error);
            alert('Logout failed');
        }
    }

    const handleGetUserDetails = async () => {
        try {
            const response = await axiosApi.get('/users/get_details');
            console.log(response);
            alert('Get user details successful');
        } catch (error) {
            console.error(error);
            alert('Get user details failed');
        }
    }

    const handleDictionaryNav = () => {
        navigate('/v2/dictionary');
    }

    const handleDeleteAccount = async () => {
        try {
            const response = await axiosApi.delete('/users/delete');
            console.log(response);
            alert('Delete account successful');

            const logoutResponse = await axiosApi.get('/users/logout');
            console.log(logoutResponse);
            alert('Logout successful');
            navigate('/v2/');

        } catch (error) {
            console.error(error);
            alert('Delete account failed');
        }
    }

    const [profilePictureId, setProfilePictureId] = useState('');

    const handleGetProfilePicture = async (event) => {

        event.preventDefault()

        try {
            const response = await axiosApi.get('/users/get_profile_picture/id=' + profilePictureId);
            // console.log(response);
            alert('Get profile picture successful');
        } catch (error) {
            // console.error(error);
            alert('Get profile picture failed');
        }
    }

    return (<div>
        <TopNavigation/>
            <h1>Home</h1>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={inputValue}
                    onChange={(e) => setInputValue(e.target.value)}
                />
                <button type="submit">Submit</button>
            </form>
            <form onSubmit={handleFileUpload}>
                <input type="file" onChange={handleFileChange}/>
                <button type="submit">Upload Image</button>
            </form>
            <button onClick={handleLogout}>Logout</button>
            <button onClick={handleDictionaryNav}>Dictionary</button>
            <button onClick={handleGetUserDetails}>Get User Details</button>
            <button onClick={handleDeleteAccount}>Delete Account</button>
            <form onSubmit={handleGetProfilePicture}>
                <input
                    type="number"
                    value={profilePictureId}
                    onChange={(e) => setProfilePictureId(e.target.value)}
                />
                <button type="submit">Get Profile Picture</button>
            </form>
        </div>);
}

export default Home;
