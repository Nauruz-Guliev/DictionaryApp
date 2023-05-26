import * as React from "react";
import {setAuthToken} from "../auth/axios_helper";
import * as EndPoints from '../constants/AppEndPoints';
import {useContext, useState} from "react";
import {UserContext} from "../context/Context";
import {useNavigate} from "react-router-dom";


export default function UserDropdown(props) {

    const [isOpen, setIsOpen] = useState(false);
    const [user, setUser] = useContext(UserContext);
    const navigate = useNavigate();

    function logout() {
        setUser(null);
        setAuthToken(null);
        navigate(null);
    }


    function toggleOpen() {
        setIsOpen(!isOpen);
    }


    const menuClass = `dropdown-menu${isOpen ? " show" : ""}`;
    return (
        <div className="dropdown" onClick={toggleOpen}>
            <button
                className="btn btn-secondary dropdown-toggle"
                type="button"
                id="dropdownMenuButton"
                data-toggle="dropdown"
                aria-haspopup="true">
                {user.firstName + " " + user.lastName}
            </button>
            <div className={menuClass} aria-labelledby="dropdownMenuButton">
                <a className="dropdown-item" href={EndPoints.PROFILE}>
                    Profile
                </a>
                <a className="dropdown-item" href={EndPoints.FAVORITES}>
                    Favorite words
                </a>
                <a className="dropdown-item" href={EndPoints.HOME} onClick={logout}>
                    Logout
                </a>
            </div>
        </div>
    );

}