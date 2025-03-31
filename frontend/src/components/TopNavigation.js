import React, {useEffect, useState} from 'react';
import './css/TopNavigation.css';
import logo from '../Resource/logo.png';
import axiosApi from "../util/AxiosRequests";

function TopNavigation(){
    const [id,setId] = useState(0);
    const [name,setName] = useState("");

    useEffect(() => {

        const getIDAndName = async () =>{
            try{
                const res = await axiosApi.get("/users/get_logged_in_user_id");
                //console.log(res);
                setId(res.data['id'])
            }
            catch (error){
                setId(-1);
            }

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
        <div className={"topNavigation"}>
            <ul>
                <a href="/v2/about"><img src={logo} alt={"logo"}
                                         style={{maxHeight: '60px', objectFit: 'contain'}}/></a>
                <li><a href="/v2">Home</a></li>
                <li><a href="/v2/detail?newpost=true">New Post</a></li>
                <li><a href="/v2/detail">All Item</a></li>
                <li><a href={`/v2/detail?myitem=${id}`}>My Item</a></li>
                <li><a href={`/v2/detail?wantbuy=${id}`}>Want Buy</a></li>
                <li><a className="active" href={name !== "" ? `/v2` :`/v2/login`}>{name === "" ? "Login" : name}</a></li>
            </ul>
        </div>
    );
}

export default TopNavigation;