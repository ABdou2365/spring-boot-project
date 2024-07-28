import {useState
, useContext,
    createContext,
    useEffect
,} from "react";
import { jwtDecode } from "jwt-decode";
import {login as performLogin} from "../../services/client.js";


const AuthContext = createContext({});

const AuthProvider = ({children}) =>{

    const [customer,setCustomer] = useState(null)

    useEffect(()=>{
        let token = localStorage.getItem("token")
        if (token){
            const decodedToken = jwtDecode(token)
            setCustomer({
                username: decodedToken.sub,
                roles: decodedToken.scopes
            })
        }
    },[])

    const SetCustomerFromToken = ()=>{
        let token = localStorage.getItem("token")
        if (token){
            const decodedToken = jwtDecode(token)
            setCustomer({
                username: decodedToken.sub,
                roles: decodedToken.scopes
            })
        }
    }


    const login = async (usernameAndPassword)=>{
        return new Promise((resolve,reject)=>{
            performLogin(usernameAndPassword).then(res=>{
                const jwtToken = res.headers["authorization"];
                const decodedToken = jwtDecode(jwtToken)
                localStorage.setItem("token",jwtToken)
                setCustomer({
                    username: decodedToken.sub,
                    roles: decodedToken.scopes
                })
                resolve(res)
                }
            ).catch(err=>{
                reject(err)
            })
        })
    }

    const logOut = ()=> {
        localStorage.removeItem("token")
        setCustomer(null)
    }

    const isCustomerAuthenticated = ()=>{
        const token = localStorage.getItem("token")

        if(!token){
            return false
        }
        const {exp:expiration}  = jwtDecode(token)

        if(Date.now() > expiration * 1000){
            logOut()
            return false
        }

        return true
    }

    return (
        <AuthContext.Provider value={{
            customer,
            login,
            logOut,
            isCustomerAuthenticated,
            SetCustomerCredentials: SetCustomerFromToken
        }}>
            {children}
        </AuthContext.Provider>
    )
}


export const useAuth = () => useContext(AuthContext)

export default AuthProvider;