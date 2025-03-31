// `frontend/src/pages/Root.js`
import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axiosApi from "../util/AxiosRequests";
import TopNavigation from "../components/TopNavigation";
import './css/Root.css';

const Root = () => {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const response = await axiosApi.get('/users/logout');
            console.log(response);
            alert('Logout successful');
            window.location.reload();
        } catch (error) {
            console.error(error);
            alert('Logout failed');
        }
    }

    const [name,setName] = useState("");

    useEffect(() => {

        const getIDAndName = async () =>{
            try {
                const res = await axiosApi.get("/users/get_username/id=0");
                //console.log(res);
                setName(res.data['username']);
            }
            catch (error)
            {
                setName("");
            }
        }
        getIDAndName()
    }, []);

    return (
        <div className="root-container">
            <TopNavigation/>
            <div className="content-container">
                <h1>Welcome to Tradebird!</h1>
                <a href="/v2/about" style={{textDecoration:"none"}}>About Us & Contact</a>
                {
                    name === "" ?
                        <button onClick={() => navigate('/v2/register')}>Register</button> :
                        <p>Welcome back, user {name}.</p>
                }
                {
                    name === "" ?
                        <button onClick={() => navigate('/v2/login')}>Login</button> :
                        <button onClick={handleLogout}>Logout</button>
                }
            </div>
        </div>
    );
};

export default Root;