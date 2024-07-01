import React, { createContext, useState, useContext } from "react"


export const AuthContext = createContext({
	user: null,
	handleLogin: (userDetails) => {},
	handleLogout: () => {}
})

export const AuthProvider = ({ children }) => {
	const [user, setUser] = useState(null)

	const handleLogin = (userDetails) => {
		localStorage.setItem("id", userDetails.id)
		localStorage.setItem("userEmail", userDetails.email)
		localStorage.setItem("userRole", userDetails.roles)

		setUser(userDetails)
	}

	const handleLogout = () => {
		localStorage.removeItem("id")
		localStorage.removeItem("userEmail")
		localStorage.removeItem("userRole")
		setUser(null)
	}

	return (
		/* cette partie du code enveloppe tous les composants enfants avec le contexte AuthContext,
		 en partageant les valeurs user, handleLogin, et handleLogout avec eux.*/
		<AuthContext.Provider value={{ user, handleLogin, handleLogout }}>
			{children}
		</AuthContext.Provider>
	)
}

export const useAuth = () => {
	return useContext(AuthContext)
}
