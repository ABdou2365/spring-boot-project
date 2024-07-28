'use client'

import {
    Button,
    Checkbox,
    Flex,
    Text,
    FormControl,
    FormLabel,
    Heading,
    Input,
    Stack,
    Image, Box, Alert, Select,
} from '@chakra-ui/react'
import {Formik, Form, useField} from "formik";
import * as Yup from "yup";
import React, {useEffect} from "react";
import {useAuth} from "../context/AuthContext.jsx";
import {redirect, useNavigate} from "react-router-dom";
import {failNotification, successNotification} from "../../services/notifications.js";
import {saveCustomer} from "../../services/client.js";
import CreateCustomerForm from "../shared/CreateCustomerForm.jsx";





export const Signup = () => {
    const {customer,login,SetCustomerCredentials} = useAuth()
    const navigate = useNavigate();

    useEffect(()=>{
        if (customer){
            navigate("/dashboard")
        }
    })

    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={4} flex={1} align={'center'} justify={'center'}>
                <Stack spacing={2} w={'full'} maxW={'md'}>
                    <Stack spacing={1}  alignItems={"center"}
                           justifyContent={"center"}>
                        <Image
                            borderRadius='full'
                            boxSize='110px'
                            src='../../public/Profile.JPG'
                            alt='abdellah Moutawakkil Picture'
                        />
                    </Stack>
                    <Heading fontSize={'2xl'}>Register for an account</Heading>
                    <CreateCustomerForm onSuccess={(token)=>{
                            localStorage.setItem("token",token);
                            SetCustomerCredentials()
                        navigate("/dashboard")
                    }}/>
                    <Stack pt={2}>
                        <Text align={'center'}>
                            Have an account?
                            <Button  color={'blue.400'} onClick={()=>{navigate("/")}}>Sign in</Button>
                        </Text>
                    </Stack>
                </Stack>
            </Flex>
            <Flex
                flex={1}
                justifyContent={"center"}
            >
                <Image
                    alt={'Login Image'}
                    objectFit={'cover'}
                    src={
                        'https://foundersorganization.com/wp-content/uploads/2022/05/cult-like-customers.png'
                    }
                />
            </Flex>
        </Stack>
    )
}