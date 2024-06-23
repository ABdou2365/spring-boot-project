import { Text} from '@chakra-ui/react'
import SidebarWithHeader from "./shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import { Stack , Spinner } from "@chakra-ui/react";
import Card from "./components/Card.jsx";
import { Wrap, WrapItem } from '@chakra-ui/react'


function App() {

    const [customers,setCustomers] = useState([]);
    const [loading,setLoading] = useState(true);


    useEffect(() => {
            setLoading(true)
            getCustomers().then(res => {
                    setCustomers(res.data)
                console.log(res.data)
                }
            ).catch(err => {
                throw err;
            }).finally(
                () => {
                    setLoading(false)
                }
                );
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
                    <Text>No customer is available</Text>
                </SidebarWithHeader>
            )
        }


        return (
            <SidebarWithHeader>
                <Wrap spacing='30px' justify='center'>
                    {
                        customers.map((customer, index) => (
                            <WrapItem key={index}>
                                <Card
                                    {...customer}
                                    ageNumber={index}
                                />
                            </WrapItem>

                        ))
                    }
                </Wrap>
            </SidebarWithHeader>
                )
}
export default App
