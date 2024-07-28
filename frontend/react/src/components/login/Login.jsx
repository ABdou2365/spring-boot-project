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
    Image, Box, Alert,
} from '@chakra-ui/react'
import {Formik, Form, useField} from "formik";
import * as Yup from "yup";
import React, {useEffect} from "react";
import {useAuth} from "../context/AuthContext.jsx";
import {redirect, useNavigate} from "react-router-dom";
import {failNotification, successNotification} from "../../services/notifications.js";


const MyTextInput = ({ label, ...props }) => {
    const [field, meta] = useField(props);
    return (
        <Box mt={2}>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status="error" mt={2}>{meta.error}</Alert>
            ) : null}
        </Box>
    );
};

const FormLogin = ()=>{
    const navigate = useNavigate();
    const {login} = useAuth()
    return(
        <Formik
            initialValues={
                {username : "" , password : ""}
            }
            validationSchema={Yup.object({
                username: Yup.string()
                    .email("Please type a valid email")
                    .required('Email is required'),
                password: Yup.string()
                    .max(20, 'Must be 20 characters or less')
                    .required('Password is required'),
            })}
            onSubmit={(values,{setSubmitting})=>{
                setSubmitting(true)
                login(values).then(
                    (res)=>{
                        navigate("/dashboard")
                        console.log(res)
                    }
                ).catch(err=>{
                    failNotification(
                        err.code,
                        err.response.data.message
                    )
                }).finally(
                    ()=>{
                        setSubmitting(false)
                    }
                )
            }} >
            {({isValid,isSubmitting})=>(
                <Form>
                    <Stack spacing="25px">
                        <MyTextInput
                            label="Email"
                            name="username"
                            type="email"
                            placeholder="abde@test.com"
                        />

                        <MyTextInput
                            label="Password"
                            name="password"
                            type="password"
                            placeholder="Type your password"

                        />
                        <Button disabled={!isValid || isSubmitting} type="submit">Login</Button>
                    </Stack>
                </Form>
            )}
        </Formik>

    )
}


export const Login = () => {
    const {customer} = useAuth()
    const navigate = useNavigate();

    useEffect(()=>{
        if (customer){
            navigate("/dashboard")
        }
    })

    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Stack spacing={4}  alignItems={"center"}
                           justifyContent={"center"}>
                        <Image
                            borderRadius='full'
                            boxSize='110px'
                            src='../../public/Profile.JPG'
                            alt='abdellah Moutawakkil Picture'
                        />
                    </Stack>
                    <Heading fontSize={'2xl'}>Sign in to your account</Heading>
                    <FormLogin />
                    <Stack pt={6}>
                        <Text align={'center'}>
                            Don't have an account?
                            <Button background={"white"} color={'blue.400'} onClick={()=>{navigate("/signup")}}>Sign Up</Button>
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