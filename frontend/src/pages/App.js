import '../css/App.css'
import logo from '../logo.svg'
import Header from "../items/Header";
import Authentication from "./Authentication";
import * as AppEndPoints from "../constants/AppEndPoints";
import Home from "./Home";
import Translation from "./Translation";
import {
    Routes,
    Route, BrowserRouter, Navigate, useNavigate
} from "react-router-dom";
import Profile from "./Profile";
import {Dictionary} from "./Dictionary";
import {useEffect, useState} from "react";
import {getUser} from "../auth/axios_helper";
import {ErrorPage} from "./ErrorPage";
import {UserContext} from "../context/Context";
import FavoriteWords from "./FavoriteWords";

function App() {
    const [user, setUser] = useState(null);
    const navigate = useNavigate()

    const [redirect, setRedirect] = useState(null);

    useEffect(() => {
        let user = getUser().then(
            value => {
                setUser(value);
            }
        ).catch(error => {
            //setRedirect(true);
            navigate(AppEndPoints.LOGIN);
        });
        setUser(user);
    }, []);

    return (
        <UserContext.Provider value={[user, setUser]}>
            <div className="w-100">
                <Header pageTitle="LLAPP" logoSrc={logo} user={user}/>
                <Routes>
                    <Route exact path={AppEndPoints.HOME} element={<Home/>}></Route>
                    <Route exact path={AppEndPoints.LOGIN} element={<Authentication/>}></Route>
                    <Route exact path={AppEndPoints.TRANSLATION} element={<Translation/>}></Route>
                    <Route exact path={AppEndPoints.PROFILE} element={<Profile/>}></Route>
                    <Route exact path={AppEndPoints.DICTIONARY} element={<Dictionary/>}></Route>
                    <Route exact path={AppEndPoints.FAVORITES} element={<FavoriteWords/>}></Route>
                    <Route exact path={AppEndPoints.ERROR} element={<ErrorPage/>}></Route>
                </Routes>
                {redirect && <Navigate to={AppEndPoints.ERROR}/>}
            </div>
        </UserContext.Provider>
    );
}

export default App;