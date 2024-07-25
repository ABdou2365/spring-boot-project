import { Text} from '@chakra-ui/react'
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import { Stack , Spinner } from "@chakra-ui/react";
import Card from "./components/Card.jsx";
import { Wrap, WrapItem } from '@chakra-ui/react'
import CreateCustomerDrawer from "./components/customer/CreateCustomerDrawer.jsx";
import {failNotification} from "./services/notifications.js";




function App() {

    console.log("Debbuging the github actionsss")
    const [customers,setCustomers] = useState([]);
    const [loading,setLoading] = useState(true);

    const fetchCustomers = () => {
        getCustomers().then(res => {
                setCustomers(res.data)
            }
        ).catch(err => {
            failNotification(
                err.code,
                err.response.data.message
            );
        }).finally(
            () => {
                setLoading(false)
            }
        );
    }


    useEffect(() => {
            setLoading(true)
            fetchCustomers()
        },[]
    )

        if (loading) {
            return (
                <SidebarWithHeader>
                    <Stack direction='row'  spacing={4}>
                        <Spinner size='xl'/>
                    </Stack>
                </SidebarWithHeader>
            )
        }

        if (customers.length <= 0) {
            return (
                <SidebarWithHeader>
                    <CreateCustomerDrawer fetchCustomers={fetchCustomers}/>
                    <Text mt={3}>No customer is available</Text>
                </SidebarWithHeader>
            )
        }


        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer fetchCustomers={fetchCustomers}/>
                <Wrap spacing='30px' justify='center'>
                    {
                        customers.map((customer, index) => (
                            <WrapItem key={index}>
                                <Card
                                    {...customer}
                                    ageNumber={index}
                                    fetchCustomers={fetchCustomers}
                                />
                            </WrapItem>

                        ))
                    }
                </Wrap>
            </SidebarWithHeader>
                )
}
export default App
