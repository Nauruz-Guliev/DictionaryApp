import * as React from 'react';
import UserDropdown from "./UserDropdown";
import * as EndPoints from '../constants/AppEndPoints';
import {UserContext} from "../context/Context";
import {useContext} from "react";

export default function Header(props) {
    const [user, setUser] = useContext(UserContext);
    return (
        <header className="w-100">
            <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                <div className="container-fluid">
                    <div className="mx-auto order-0 pe-4 ps-2">
                        <a className="navbar-brand mx-auto" href={EndPoints.HOME}>{props.pageTitle}</a>
                        <button className="navbar-toggler" type="button" data-toggle="collapse"
                                data-target=".dual-collapse2">
                            <span className="navbar-toggler-icon"></span>
                        </button>
                    </div>
                    <AuthButtons user={user}/>
                </div>
            </nav>
        </header>
    );
}

const AuthButtons = (props) => {
    const [user, setUser] = useContext(UserContext);
    if (user) {
        return (
            <div className="navbar-collapse collapse w-100 order-3 dual-collapse2">
                <ul className="navbar-nav ms-auto">
                    <UserDropdown user={user}/>
                </ul>
            </div>
        )
    } else {
        return (
            <div className="navbar-collapse collapse w-100 order-3 dual-collapse2">
                <ul className="navbar-nav ms-auto">
                    <li className="nav-item">
                        <a className="nav-link" href={EndPoints.LOGIN}>Login</a>
                    </li>
                </ul>
            </div>
        )
    }
};

