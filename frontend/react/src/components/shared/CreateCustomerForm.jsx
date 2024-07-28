import React from 'react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { Alert, Box, Button, FormLabel, Input, Select, Stack } from "@chakra-ui/react";
import {saveCustomer} from "../../services/client.js";
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

const MySelect = ({ label, ...props }) => {
    const [field, meta] = useField(props);
    return (
        <Box mt={2}>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" mt={2} status="error">{meta.error}</Alert>
            ) : null}
        </Box>
    );
};

const CreateCustomerForm = ({onSuccess}) => {
    return (
        <Formik
            initialValues={{
                name: '',
                email: '',
                age: 0,
                password:'',
                gender: '',
            }}
            validationSchema={Yup.object({
                name: Yup.string()
                    .max(20, 'Must be 20 characters or less')
                    .required('Required'),
                email: Yup.string()
                    .email('Invalid email address')
                    .required('Required'),
                age: Yup.number()
                    .min(16, 'Must be at least 16 years old!')
                    .max(100, 'Must be less than 100 years old!')
                    .required('Required'),
                password: Yup.string()
                    .max(20, 'Must be 20 characters or less')
                    .min(8,"Must be 8 characters or more")
                    .required('Required'),
                gender: Yup.string()
                    .oneOf(['MALE', 'FEMALE'], 'Invalid Gender')
                    .required('Required'),
            })}
            onSubmit={(customer, { setSubmitting }) => {
                setSubmitting(true)
                saveCustomer(customer)
                    .then(res =>{
                        successNotification("customer saved",`${customer.name} was successfully saved`
                            )
                        console.log(res)
                    onSuccess(res.headers["authorization"])
                }
                ).catch(err =>{
                    failNotification(
                        err.code,
                        err.response.data.message
                        )
                }).finally(()=>{
                    setSubmitting(false)
                })

            }}
        >
            {({ isValid, isSubmitting }) => (
                <Form>
                    <Stack spacing="25px">
                        <MyTextInput
                            label="Name"
                            name="name"
                            type="text"
                            placeholder="Jane"
                        />

                        <MyTextInput
                            label="Email Address"
                            name="email"
                            type="email"
                            placeholder="jane@formik.com"
                        />

                        <MyTextInput
                            label="Age"
                            name="age"
                            type="number"
                            placeholder="20"
                        />

                        <MyTextInput
                            label="Password"
                            name="password"
                            type="password"
                            placeholder="Type a secure password"
                        />

                        <MySelect label="Gender" name="gender">
                            <option value="">Select a gender</option>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                        </MySelect>

                        <Button disabled={!isValid || isSubmitting} type="submit">Submit</Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};

export default CreateCustomerForm;
