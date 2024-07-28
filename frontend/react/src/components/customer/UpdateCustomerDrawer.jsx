import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, Input, useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "../shared/CreateCustomerForm.jsx";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";


const CloseIcon = () => "X"


const UpdateCustomerDrawer = ({initialValues,id,fetchCustomers}) =>{
    const { isOpen, onOpen, onClose } = useDisclosure()
    return(
        <>
            <Button colorScheme='green' onClick={onOpen} rounded={"full"} mt={8}>Update</Button>
            <Drawer isOpen={isOpen}  size={"xl"}  >
                <DrawerContent>
                    <DrawerHeader>Create your account</DrawerHeader>

                    <DrawerBody>
                        <UpdateCustomerForm initialValues={initialValues}  id={id}  fetchCustomers={fetchCustomers}/>
                    </DrawerBody>

                    <DrawerFooter>
                        <Button type='submit' form='my-form' onClick={onClose} leftIcon={CloseIcon}>
                            Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    )
}


export default UpdateCustomerDrawer