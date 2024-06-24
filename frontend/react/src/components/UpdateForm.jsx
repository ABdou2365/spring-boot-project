import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, Input, useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";


const CloseIcon = () => "X"


const UpdateForm = ({name,age,email,id,gender,fetchCustomers}) =>{
    const { isOpen, onOpen, onClose } = useDisclosure()
    return(
        <>
            <Button colorScheme='green' onClick={onOpen} rounded={"full"} mt={8}>Update</Button>
            <Drawer isOpen={isOpen}  size={"xl"}  >
                <DrawerContent>
                    <DrawerHeader>Create your account</DrawerHeader>

                    <DrawerBody>
                        <UpdateCustomerForm name={name} gender={gender} age={age} id={id} email={email} fetchCustomers={fetchCustomers}/>
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


export default UpdateForm