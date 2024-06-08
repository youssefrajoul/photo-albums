$(document).ready(function () {

    const indexedDB =
        window.indexedDB ||
        window.mozIndexedDB ||
        window.webkitIndexedDB ||
        window.msIndexedDB;


    if (!indexedDB) {
        console.log("Your browser doesn't support a stable version of IndexedDB.");
    } else {
        // indexedDB.deleteDatabase("usersDB")
        const request = indexedDB.open("usersDB", 1)
        console.log("open")

        //Create the schema of db
        request.onupgradeneeded = function (event) {
            const db = event.target.result;
            console.log("create");
            // Create an object store named "users" with an auto-incrementing key
            const objectStore = db.createObjectStore("users", { keyPath: "id", autoIncrement: true });
            // Create an index to search users by name
            objectStore.createIndex("name", "name", { unique: true });
        };

        $("#btn-register").click(function () {
            var username = $('#username').val();
            var password = $('#password').val();

            generateKeyPair(username, password);
        })

        $("#btn-login").click(function () {
            const loginRequest = indexedDB.open("usersDB", 1)
            //get user name if existe and decrypte his key
            console.log("login");
            var username = $('#username').val();
            var password = $('#password').val();

            //check if the username existe and has the necessary data
            loginRequest.onsuccess = function (event) {
                const db = event.target.result;
                if (db.objectStoreNames.contains("users")) {
                    const transaction = db.transaction(["users"], "readwrite");
                    const objectStore = transaction.objectStore("users");

                    const index = objectStore.index("name");
                    const getRequest = index.get(username);
                    getRequest.onsuccess = function (event) {
                        const user = event.target.result
                        if (user) {
                            //get the encrypted private key: decrypt and store it
                            decryptPrivateKey(user.encryptedPrivateKey, password, user.salt, user.iv, username)

                        } else {
                            alert("User not found try to register")
                        }
                    }

                }
            }
        })
    }

    //Function that generate a pair of key base64 and return them as JSON
    async function generateKeyPair(username, password) {
        try {
            // Generate key pair
            const keyPair = await window.crypto.subtle.generateKey(
                {
                    name: "RSA-OAEP",
                    modulusLength: 2048,
                    publicExponent: new Uint8Array([1, 0, 1]),
                    hash: "SHA-256"
                },
                true,
                ["encrypt", "decrypt"]
            );

            // Export public and private keys
            const publicKey = await window.crypto.subtle.exportKey("spki", keyPair.publicKey);
            const privateKey = await window.crypto.subtle.exportKey("pkcs8", keyPair.privateKey);

            // Convert keys to base64 for storage
            const publicKeyBase64 = btoa(String.fromCharCode.apply(null, new Uint8Array(publicKey)));
            const privateKeyBase64 = btoa(String.fromCharCode.apply(null, new Uint8Array(privateKey)));

            encryptPrivateKey(username, privateKeyBase64, password, publicKeyBase64);

        } catch (error) {
            alert("ERR");
            console.error("Error generating key pair:", error);
        }
    }

    async function encryptPrivateKey(username, privateKey, password, publicKey) {
        // Encode the private key string into a byte array
        const encodedPrivateKey = new TextEncoder().encode(privateKey);

        // Generate a random salt
        const salt = generateRandomSalt(16); // 16 bytes (128 bits)

        // Derive a key from the password using PBKDF2
        const keyMaterial = await getKeyMaterial(password, salt);

        // Generate a random IV
        const iv = generateRandomIV(12); // 12 bytes (96 bits)

        // Encrypt the private key using AES-GCM with the derived key and IV
        const encryptedPrivateKey = await window.crypto.subtle.encrypt(
            { name: "AES-GCM", iv: iv },
            keyMaterial,                    //the key used to encrypt the private key
            encodedPrivateKey
        );
        const request = indexedDB.open("usersDB", 1);

        request.onsuccess = function (event) {
            const db = event.target.result;
            // Ensure the object store exists before trying to perform a transaction
            if (db.objectStoreNames.contains("users")) {
                const transaction = db.transaction(["users"], "readwrite");
                const objectStore = transaction.objectStore("users");

                const index = objectStore.index("name");
                const getRequest = index.get(username);
                getRequest.onsuccess = function (event) {
                    if (event.target.result) {
                        console.log("Username already exists");
                        alert("Username already exists. Please choose another username.");
                    } else {
                        objectStore.put(
                            {
                                name: username
                                , iv: iv
                                , publicKey: publicKey
                                , salt: salt
                                , encryptedPrivateKey: encryptedPrivateKey
                                , privateKey: null
                            });
                        console.log("user created ;)")
                        console.log(objectStore.get(1));

                        //send to register the user
                        const user = {
                            username: username,
                            password: password,
                            publicKey: publicKey
                        };
                        fetch("https://localhost:8443/api/signup-request", {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(user)
                        })
                            .then(data => {
                                console.log('Success:', data);
                                console.log("token: ", data.token)
                            })

                    }
                };


            } else {
                alert("no data base is called users")
            }
        };
    }

    // Decrypt the encrypted private key using AES-GCM with PBKDF2 for key derivation
    async function decryptPrivateKey(encryptedPrivateKey, password, salt, iv, username) {
        try {
            // Derive a key from the password using PBKDF2 and the provided salt
            const keyMaterial = await getKeyMaterial(password, salt);

            // Decrypt the encrypted private key using AES-GCM with the derived key and IV
            const decryptedPrivateKey = await window.crypto.subtle.decrypt(
                { name: "AES-GCM", iv: iv },
                keyMaterial,
                encryptedPrivateKey
            );

            const privateKey = new TextDecoder().decode(decryptedPrivateKey);

            const request = indexedDB.open("usersDB", 1)
            request.onsuccess = function (event) {
                const db = event.target.result;
                const transaction = db.transaction(["users"], "readwrite");
                const objectStore = transaction.objectStore("users");

                // Get the index for searching users by name
                const index = objectStore.index("name");

                // Get the user with the specified name from the index
                const getRequest = index.get(username);

                getRequest.onsuccess = function (event) {
                    const user = event.target.result;
                    console.log(user)
                    if (user) {
                        //update the private key
                        user.privateKey = privateKey;
                        //store the update in the DB
                        const putRequest = objectStore.put(user);

                        putRequest.onsuccess = function () {
                            console.log("User updated successfully.");
                        };

                        putRequest.onerror = function (event) {
                            console.log("Error updating user:");
                            // Log the event for more detailed debugging
                            console.error(event);
                        };
                        //Todo: Get the token from the server
                        const user = {
                            username: username,
                            password: password,
                        };
                        fetch("https://localhost:8443/api/login-request", {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(user)
                        })
                            .then(data => {
                                console.log('Success:', data);
                                console.log("token: ", data.token)
                            })

                    }
                    console.log("user geted ;)")
                    console.log(user)
                }

            }
        } catch (error) {
            console.log("error")
        }
    }

    // Generate a random salt
    function generateRandomSalt(length) {
        const salt = new Uint8Array(length);
        window.crypto.getRandomValues(salt);
        return salt;
    }

    // Generate a random IV
    function generateRandomIV(length) {
        const iv = new Uint8Array(length);
        window.crypto.getRandomValues(iv);
        return iv;
    }

    // Derive a key from the provided password using PBKDF2
    async function getKeyMaterial(password, salt) {
        const encoder = new TextEncoder();
        const passwordBuffer = encoder.encode(password);

        const keyMaterial = await window.crypto.subtle.importKey(
            "raw",
            passwordBuffer,
            { name: "PBKDF2" },
            false,
            ["deriveBits", "deriveKey"]
        );

        return window.crypto.subtle.deriveKey(
            { name: "PBKDF2", salt: salt, iterations: 100000, hash: "SHA-256" },
            keyMaterial,
            { name: "AES-GCM", length: 256 },
            true,
            ["encrypt", "decrypt"]
        );
    }



    //it will be with logout                                            !!!!!
    //delete the private key
    // const visibilityChangeListener = (event) => {
    //     //destroye the password of the login user in indexedDB
    //     const rq = indexedDB.open("users", 1)
    //     rq.onsuccess = function (event) {
    //         const db = event.target.result;
    //         const transaction = db.transaction(["users"], "readwrite");
    //         const objectStore = transaction.objectStore("users");

    //         // a cursor iterate over all records in the object store
    //         const cursorRequest = objectStore.openCursor();

    //         getRequest.onsuccess = function (event) {
    //             const cursor = event.target.result;
    //             if (cursor) {
    //                 //take each user
    //                 const user = cursor.value;
    //                 user.privateKey = null

    //                 // Update the user record
    //                 const updateRequest = cursor.update(user);
    //                 updateRequest.onsuccess = function () {
    //                     console.log("Private key deleted for user:", user);
    //                 };
    //                 updateRequest.onerror = function (event) {
    //                     console.error("Error updating user:", event.target.error);
    //                 };

    //                 // Move to the next record
    //                 cursor.continue();
    //             }
    //         }
    //     }
    // }


    // window.addEventListener("visibilitychange", visibilityChangeListener);


})
